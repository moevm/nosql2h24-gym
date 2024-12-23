package com.example.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.gym.model.settings.LoyaltySettings;

@Repository
public interface LoyaltySettingsRepository extends MongoRepository<LoyaltySettings, String> {

}
