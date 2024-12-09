package com.example.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gym.model.settings.LoyaltySettings;

public interface LoyaltySettingsRepository extends MongoRepository<LoyaltySettings, String> {

}
