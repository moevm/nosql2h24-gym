package com.example.gym.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.settings.LoyaltySettings;
import com.example.gym.model.subscription.SubscriptionStatus;
import com.example.gym.model.subscription.dto.CreateSubscriptionDto;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.subscription.dto.UpdateSubscriptionDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.Subscription;
import com.example.gym.repository.LoyaltySettingsRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final Mapper modelMapper;
    private final LoyaltySettingsRepository loyaltySettingsRepository;

    @Transactional
    public ResponseClientDto createSubscription(CreateSubscriptionDto dto, String clientId) throws ResourceNotFoundException {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент с id %s не найден".formatted(clientId)));
        
        ClientInfo clientInfo = client.getClientInfo();
        LocalDateTime now = LocalDateTime.now();
        Subscription subscription = new Subscription(
            now,
            now.plusDays(dto.getDuration()),
            SubscriptionStatus.ACTIVE,
            dto.getPrice(),
            null,
            LocalDateTime.now(), 
            LocalDateTime.now());

        clientInfo.setSubscriptions(List.of(subscription));

        List<LoyaltySettings> loyaltySettings = loyaltySettingsRepository.findAll();

        if (!loyaltySettings.isEmpty()) {
            LoyaltySettings loyaltySetting = loyaltySettings.get(0);
            
            Double loyalty = dto.getPrice() * loyaltySetting.getAcceptanceRate();
            Integer bonus = 0;
            if (loyalty < 1d) {
                bonus = 0;
            } else {
                bonus = (int) Math.ceil(loyalty);
            }

            if (clientInfo.getLoyaltyPoints() == null) {
                clientInfo.setLoyaltyPoints(0);
            } 

            clientInfo.setLoyaltyPoints(clientInfo.getLoyaltyPoints() + bonus);
        }

        client.setClientInfo(clientInfo);
        User savedClient = userRepository.save(client);
        return modelMapper.toClientDto(savedClient);
    }

    public ResponseSubscriptionDto findByClientId(String clientId) {
        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент с id %s не найден".formatted(clientId)));

        ClientInfo clientInfo = user.getClientInfo();
        if (clientInfo == null) {
            return new ResponseSubscriptionDto();
        }

        List<Subscription> subscriptions = clientInfo.getSubscriptions();

        if (subscriptions.isEmpty()) {
            return new ResponseSubscriptionDto();
        }

        Subscription subscription = subscriptions.get(0);

        return modelMapper.toDto(subscription, clientId);
    }

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

    @Transactional
    public ResponseSubscriptionDto extendSubscription(UpdateSubscriptionDto dto, String clientId) {
        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент с id %s не найден".formatted(clientId)));

        ClientInfo clientInfo = user.getClientInfo();

        if (clientInfo == null) {
            throw new ResourceNotFoundException("У клиента нет информации.");
        }

        List<Subscription> subscriptions = clientInfo.getSubscriptions();
        if (subscriptions.isEmpty()) {
            throw new ResourceNotFoundException("У клиента нет абонементов.");
        }

        Subscription subscription = subscriptions.get(0);

        LocalDateTime now = LocalDateTime.now();

        Integer restDays = subscription.getRestDays();
        if (restDays <= 0) {
            subscription.setStartDate(now);
            subscription.setEndDate(now.plusDays(dto.getDuration()));
        } else {
            Integer additionalDuration = dto.getDuration();
            subscription.setEndDate(subscription.getEndDate().plusDays(additionalDuration));
        }

        subscription.setStatus(SubscriptionStatus.ACTIVE);

        clientInfo.setSubscriptions(List.of(subscription));
        user.setClientInfo(clientInfo);
        userRepository.save(user);

        return modelMapper.toDto(subscription, clientId);
    }

    @Transactional
    public ResponseSubscriptionDto  freezeSubscription(String clientId) {
        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент с id %s не найден".formatted(clientId)));

        ClientInfo clientInfo = user.getClientInfo();

        if (clientInfo == null) {
            throw new ResourceNotFoundException("У клиента нет информации.");
        }

        List<Subscription> subscriptions = clientInfo.getSubscriptions();
        if (subscriptions.isEmpty()) {
            throw new ResourceNotFoundException("У клиента нет абонементов.");
        }

        Subscription subscription = subscriptions.get(0);

        if (subscription.getStatus() == SubscriptionStatus.ACTIVE) {
            subscription.setStatus(SubscriptionStatus.FREEZE);
            subscription.setFreezeDate(LocalDateTime.now());

            subscriptions.set(0, subscription);
            clientInfo.setSubscriptions(subscriptions);
            user.setClientInfo(clientInfo);
            userRepository.save(user);
        } else if (subscription.getStatus() == SubscriptionStatus.FREEZE) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            LocalDateTime freezeDate = subscription.getFreezeDate();
            LocalDateTime oldEndDate = subscription.getEndDate();

            LocalDateTime now = LocalDateTime.now();
            long daysBetween = ChronoUnit.DAYS.between(freezeDate, now);
            subscription.setEndDate(oldEndDate.plusDays(daysBetween));

            subscriptions.set(0, subscription);
            clientInfo.setSubscriptions(subscriptions);
            user.setClientInfo(clientInfo);
            userRepository.save(user);
        }
        
        return modelMapper.toDto(subscription, clientId);
    }

}
