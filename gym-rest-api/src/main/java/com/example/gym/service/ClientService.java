package com.example.gym.service;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.client.ResponseClientForStatistic;
import com.example.gym.model.client.UpdateClientDto;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.training.dto.ResponseTrainingForClientDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.persistence.NoResultException;
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
        return userRepository.findAllByRoleIndex(ROLE_USER_INDEX).stream()
                .map(c -> modelMapper.toClientDto(c))
                .toList();
    }

    public ResponseClientDto findClientById(String id) {
        User client = getById(id);
        return modelMapper.toClientDto(client);
    }

    public void deleteClient(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public ResponseClientDto updateClient(String id, UpdateClientDto dto) {
        User client = getById(id);
        
        if (!client.getName().equals(dto.getName()) && dto.getName() != null) {
            client.setName(dto.getName());
        }

        if (!client.getSurname().equals(dto.getSurname()) && dto.getSurname() != null) {
            client.setSurname(dto.getSurname());
        }

        if (!client.getEmail().equals(dto.getEmail()) && dto.getEmail() != null) {
            if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с такой электронной почтой уже существует");
            } 

            client.setEmail(dto.getEmail());
        }

        if (!client.getPhoneNumber().equals(dto.getPhoneNumber()) && dto.getPhoneNumber() != null) {
            if (uniquenessCheckService.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с таким номер телефона уже существует");
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

    public User getById(String id) {
        Optional<User> optionalClient = userRepository.findById(id);
        if (optionalClient.isEmpty()) {
            throw new NoResultException("Клиент с id %s не найден".formatted(id));
        }

        return optionalClient.get();
    }

}
