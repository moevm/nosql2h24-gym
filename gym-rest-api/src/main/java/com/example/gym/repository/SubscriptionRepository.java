package com.example.gym.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.example.gym.model.subscription.Subscription;
import com.example.gym.model.user.User;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    @Modifying
    void deleteById(@Param("id") String id);
    
    @Modifying
    void deleteByClient(@Param("client") User client);

    Optional<Subscription> findByClientId(String clientId);
    
}
