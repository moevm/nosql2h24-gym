package com.example.gym.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.client.UpdateClientDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
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

    private final static Integer ROLE_USER_INDEX = UserRoleType.ROLE_USER.ordinal();

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

    public List<ResponseClientDto> findAllClients() {
        return userRepository.findAllByRoles(UserRoleType.ROLE_USER.name()).stream()
                .map(c -> modelMapper.toClientDto(c))
                .toList();
    }

    public ResponseClientDto findClientById(String id) throws ResourceNotFoundException {
        return modelMapper.toClientDto(getById(id));
    }

    public void deleteClient(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public ResponseClientDto updateClient(String id, UpdateClientDto dto) throws ResourceNotFoundException, UniquenessViolationException {
        User client = getById(id);
        
        if (dto.getName() != null && !client.getName().equals(dto.getName())) {
            client.setName(dto.getName());
        }

        if (dto.getSurname() != null && !client.getSurname().equals(dto.getSurname())) {
            client.setSurname(dto.getSurname());
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

        User updatedClient = userRepository.save(client);
        return modelMapper.toClientDto(updatedClient);
    }

    // public List<ResponseTrainingForClientDto> findClientTrainings(String id) {
    //     User client = getById(id);
    //     return client.getTrainings().stream()
    //             .map(t -> modelMapper.toDtoWithTrainer(t))
    //             .toList();
    // }

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

    // public ResponseSubscriptionDto findClientSubscription(String id) {
    //     User client = getById(id);
    //     Subscription subscription = client.getSubscription();
    //     if (subscription == null) {
    //         return new ResponseSubscriptionDto();
    //     } 

    //     return modelMapper.toDto(subscription);
    // }

    public User getById(String id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент с id %s не найден".formatted(id)));
    }

}
