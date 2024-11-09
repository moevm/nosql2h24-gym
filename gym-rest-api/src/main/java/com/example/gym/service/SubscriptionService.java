package com.example.gym.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.subscription.SubscriptionStatus;
import com.example.gym.model.subscription.dto.CreateSubscriptionDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.Subscription;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final Mapper modelMapper;

    @Transactional
    public ResponseClientDto createSubscription(CreateSubscriptionDto dto, String clientId) {
        Optional<User> optionalClient = userRepository.findById(clientId);
        if (optionalClient.isEmpty()) {
            throw new NoResultException("Клиент с id %s не найден".formatted(clientId));
        }
        
        User client = optionalClient.get();
        ClientInfo clientInfo = client.getClientInfo();
        List<Subscription> subscriptions = clientInfo.getSubscription();
        LocalDate now = LocalDate.now();
        subscriptions.add(new Subscription(
            now,
            now.plusDays(dto.getDuration()),
            SubscriptionStatus.ACTIVE,
            dto.getPrice(),
            null, 
            null
        ));

        User savedClient = userRepository.save(client);
        return modelMapper.toClientDto(savedClient);
    }

    // public ResponseSubscriptionDto findById(String id) {
    //     Subscription subscription = getById(id);
    //     return modelMapper.toDto(subscription);
    // }

    // public List<ResponseSubscriptionDto> findAllSubscription() {
    //     return subscriptionRepository.findAll().stream()
    //             .map(s -> modelMapper.toDto(s))
    //             .toList();
    // }

    // public void deleteSubscriptionByClient(String clientId) {
    //     Optional<User> client = userRepository.findById(clientId);
    //     if (client.isPresent()) {
    //         subscriptionRepository.deleteByClient(client.get());
    //     }
    // }

    // public void deleteSubscriptionById(String id) {
    //     subscriptionRepository.deleteById(id);
    // }

    // @Transactional
    // public ResponseSubscriptionDto extendSubscription(UpdateSubscriptionDto dto, String id) {
    //     Subscription subscription = getById(id);

    //     Integer restDays = subscription.getRestDays();
    //     LocalDate now = LocalDate.now();
    //     if (restDays <= 0) {
    //         subscription.setDuration(dto.getDuration());
    //         subscription.setStartDate(now);
    //         subscription.setEndDate(now.plusDays(dto.getDuration()));
    //     } else {
    //         Integer restDuration = subscription.getRestDays();
    //         subscription.setDuration(dto.getDuration()+subscription.getDuration());
    //         subscription.setEndDate(subscription.getStartDate().plusDays(dto.getDuration() + restDuration));
    //     }

    //     Subscription extendedSubscription = subscriptionRepository.save(subscription);
    //     return modelMapper.toDto(extendedSubscription);
    // }

    // public Subscription getById(String id) {
    //     Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);
    //     if (optionalSubscription.isEmpty()) {
    //         throw new NoResultException("Абонемент с id %s не найден".formatted(id));
    //     }

    //     return optionalSubscription.get();
    // }

}
