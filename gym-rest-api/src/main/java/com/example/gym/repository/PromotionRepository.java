package com.example.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gym.model.promotion.Promotion;

public interface PromotionRepository extends MongoRepository<Promotion, String> {

}
