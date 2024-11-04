package com.example.gym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gym.model.training.Training;

public interface TrainingRepository extends MongoRepository<Training, String> {

    List<Training> findAllByTrainerId(String trainerId);
    Optional<Training> findByIdAndTrainerId(String id, String trainingId);

}
