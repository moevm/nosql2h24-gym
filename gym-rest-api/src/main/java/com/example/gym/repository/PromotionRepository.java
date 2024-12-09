package com.example.gym.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.gym.model.promotion.Promotion;

public interface PromotionRepository extends MongoRepository<Promotion, String> {

    @Query("{ 'createdBy.id': ?0 }")
    List<Promotion> findAllByCreatedById(String id);
    
}
