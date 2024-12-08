package com.example.gym.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.gym.exception.InvalidDataException;
import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.client.UpdateClientDto;
import com.example.gym.model.subscription.SubscriptionStatus;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.ResponseTrainingForClientDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.model.user.pojo.Subscription;
import com.example.gym.repository.TrainingRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UniquenessCheckService uniquenessCheckService;
    private final UserRepository userRepository;
    private final Mapper modelMapper;
    private final MongoTemplate mongoTemplate;
    private final TrainingRepository trainingRepository;

    // @Transactional
    // public ResponseClientDto createClient(CreateClientDto createClientDto) {
    //     if (!findClientByEmail(createClientDto.getEmail()).isEmpty()) {
    //         throw new IllegalArgumentException("Пользователь с таким email уже существует");
    //     }

    //     if (!findClientByPhoneNumber(createClientDto.getPhoneNumber()).isEmpty()) {
    //         throw new IllegalArgumentException("Пользователь с таким номером телефона уже существует");
    //     }

    //     Client client = modelMapper.toModel(createClientDto);
    //     Client createdClient = clientRepository.save(client);
    //     return modelMapper.toDto(createdClient);
    // }

    public List<ResponseClientDto> findAllClients(
            String name,
            String surname, 
            Optional<GenderType> gender,
            LocalDateTime birthdayFrom,
            LocalDateTime birthdayBefore
    ) {
        Query query = new Query();
        if (name != null && !name.isEmpty() && !name.isBlank()) 
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        
        if (surname != null && !surname.isEmpty() && !surname.isBlank())
            query.addCriteria(Criteria.where("surname").regex(surname, "i"));
        
        if (gender.isPresent())
            query.addCriteria(Criteria.where("gender").is(gender.get()));
        
        if (birthdayFrom != null) {
            query.addCriteria(Criteria.where("birthday").gte(birthdayFrom));
        }
        
        if (birthdayBefore != null) {
            query.addCriteria(Criteria.where("birthday").lt(birthdayBefore));
        }

        query.addCriteria(Criteria.where("roles").in(UserRoleType.ROLE_USER));
        
        List<User> clients = mongoTemplate.find(query, User.class);
        
        return clients.stream()
            .map(modelMapper::toClientDto)
            .collect(Collectors.toList());
    }

    public ResponseClientDto findClientById(String id) throws ResourceNotFoundException {
        return modelMapper.toClientDto(getById(id));
    }

    public void deleteClient(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && user.get().getClientInfo() != null &&  
                !user.get().getClientInfo().getSubscriptions().isEmpty() &&
                user.get().getClientInfo().getSubscriptions().get(0).getStatus() == SubscriptionStatus.ACTIVE) {
            throw new InvalidDataException("Нельзя удалить пользователя с активным абонементом");
        } else {
            userRepository.deleteById(id);
        }
    }

    @Transactional
    public ResponseClientDto updateClient(String id, UpdateClientDto dto) throws ResourceNotFoundException, UniquenessViolationException {
        User client = getById(id);

        boolean needUpdate = false;
        
        if (dto.getName() != null && !client.getName().equals(dto.getName())) {
            client.setName(dto.getName());
            needUpdate = true;
        }

        if (dto.getSurname() != null && !client.getSurname().equals(dto.getSurname())) {
            client.setSurname(dto.getSurname());
            needUpdate = true;
        }

        if (dto.getEmail() != null && !client.getEmail().equals(dto.getEmail())) {
            if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
                throw new UniquenessViolationException("Пользователь с электронной почтой %s уже существует"
                        .formatted(dto.getEmail()));
            } 

            client.setEmail(dto.getEmail());
        }

        if (dto.getPhoneNumber() != null && !client.getPhoneNumber().equals(dto.getPhoneNumber())) {
            if (uniquenessCheckService.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
                throw new UniquenessViolationException("Пользователь с номером телефона %s уже существует"
                        .formatted(dto.getPhoneNumber()));
            } 

            client.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getGender() != null && client.getGender() != dto.getGender()) {
            client.setGender(dto.getGender());
            needUpdate = true;
        }

        if (dto.getBirthday() != null && (client.getBirthday() != null && !client.getBirthday().equals(dto.getBirthday()))) {
            client.setBirthday(dto.getBirthday());
        }

        User updatedClient = userRepository.save(client);

        if (needUpdate) {
            List<Training> trainings = trainingRepository.findAllByClientId(updatedClient.getId());
            trainings.forEach(training -> {
                ClientPojo clientPojo = new ClientPojo();
                clientPojo.setId(updatedClient.getId());
                clientPojo.setName(updatedClient.getName());
                clientPojo.setSurname(updatedClient.getSurname());
                // loyaltyPoints и registrationDate не обновляем, так как они не меняются при обновлении клиента
                training.getClients().removeIf(c -> c.getId().equals(updatedClient.getId()));
                training.getClients().add(clientPojo);
                trainingRepository.save(training);
            });
        } 
        return modelMapper.toClientDto(updatedClient);
    }

    public List<ResponseTrainingForClientDto> findClientTrainings(String id) {
        getById(id);

        List<Training> trainings = trainingRepository.findAllByClientId(id);
        return trainings.stream()
                .map(modelMapper::toTrainingForClientDto)
                .toList();
    }

    // public List<ResponseClientForStatistic> getClientsActivity() {
    //     List<User> clients = userRepository.findAllByRoleIndex(ROLE_USER_INDEX);
    //     return clients.stream()
    //             .map(c -> {
    //                 ResponseClientForStatistic responseClientForStatistic = new ResponseClientForStatistic();
    //                 responseClientForStatistic.setClient(modelMapper.toClientDto(c));
    //                 responseClientForStatistic.setCount(c.getTrainings().size());
    //                 responseClientForStatistic.setTrainings(c.getTrainings().stream()
    //                         .map(t -> modelMapper.toDto(t))
    //                         .toList());  
    //                 return responseClientForStatistic;
    //             })
    //             .toList();
    // }

    public ResponseSubscriptionDto findClientSubscription(String id) {
        User client = getById(id);
        ClientInfo clientInfo = client.getClientInfo();
        if (clientInfo == null) {
            return new ResponseSubscriptionDto();
        }

        List<Subscription> subscription = clientInfo.getSubscriptions();

        if (subscription.size() == 0) {
            return new ResponseSubscriptionDto();
        }

        return modelMapper.toDto(subscription.get(0), client.getId());
    }

    public User getById(String id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент с id %s не найден".formatted(id)));
    }

}
