package com.example.gym.service;

import com.example.gym.model.subscription.Subscription;
import com.example.gym.model.subscription.dto.CreateSubscriptionDto;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.subscription.dto.UpdateSubscriptionDto;
import com.example.gym.model.user.User;
import com.example.gym.repository.SubscriptionRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final Mapper modelMapper;

    @Transactional
    public ResponseSubscriptionDto createSubscription(CreateSubscriptionDto dto, String clientId) {
        Optional<User> optionalClient = userRepository.findById(clientId);
        if (optionalClient.isEmpty()) {
            throw new NoResultException("Клиент с id %s не найден".formatted(clientId));
        }

        if (subscriptionRepository.findByClientId(clientId).isPresent()) {
            throw new NoResultException("У этого клиента уже есть абонемент");
        }
        Subscription subscription = modelMapper.toModel(dto);
        subscription.setClient(optionalClient.get());

        LocalDate now = LocalDate.now();
        subscription.setStartDate(now);
        subscription.setEndDate(now.plusDays(dto.getDuration()));

        Subscription createdSubscription = subscriptionRepository.save(subscription);
        return modelMapper.toDto(createdSubscription);
    }

    public ResponseSubscriptionDto findById(String id) {
        Subscription subscription = getById(id);
        return modelMapper.toDto(subscription);
    }

    public List<ResponseSubscriptionDto> findAllSubscription() {
        return subscriptionRepository.findAll().stream()
                .map(s -> modelMapper.toDto(s))
                .toList();
    }

    public void deleteSubscriptionByClient(String clientId) {
        Optional<User> client = userRepository.findById(clientId);
        if (client.isPresent()) {
            subscriptionRepository.deleteByClient(client.get());
        }
    }

    public void deleteSubscriptionById(String id) {
        subscriptionRepository.deleteById(id);
    }

    @Transactional
    public ResponseSubscriptionDto extendSubscription(UpdateSubscriptionDto dto, String id) {
        Subscription subscription = getById(id);

        Integer restDays = subscription.getRestDays();
        LocalDate now = LocalDate.now();
        if (restDays <= 0) {
            subscription.setDuration(dto.getDuration());
            subscription.setStartDate(now);
            subscription.setEndDate(now.plusDays(dto.getDuration()));
        } else {
            Integer restDuration = subscription.getRestDays();
            subscription.setDuration(dto.getDuration()+subscription.getDuration());
            subscription.setEndDate(subscription.getStartDate().plusDays(dto.getDuration() + restDuration));
        }

        Subscription extendedSubscription = subscriptionRepository.save(subscription);
        return modelMapper.toDto(extendedSubscription);
    }

    public Subscription getById(String id) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);
        if (optionalSubscription.isEmpty()) {
            throw new NoResultException("Абонемент с id %s не найден".formatted(id));
        }

        return optionalSubscription.get();
    }

}
