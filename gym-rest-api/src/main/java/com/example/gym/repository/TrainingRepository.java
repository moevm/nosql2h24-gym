package com.example.gym.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.gym.model.training.Training;

public interface TrainingRepository extends MongoRepository<Training, String> {

    @Query("{ 'trainer.id': ?0 }")
    List<Training> findAllByTrainerId(String trainerId);

    @Query("{ 'clients.id': ?0 }")
    List<Training> findAllByClientId(String clientId);

    @Query("{ 'room.id': ?0 }")
    List<Training> findAllByRoomId(String roomId);

    @Query("{ 'startTime': { $gte: ?0, $lte: ?1 }, 'endTime': { $lt: ?2 } }")
    List<Training> findAllByStartTimeBetweenAndEndTimeBefore(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime now);

    @Query("{ 'startTime': { $gte: ?0, $lte: ?1 }, 'endTime': { $lt: ?2 }, 'trainer.id': ?3 }")
    List<Training> findAllByStartTimeBetweenAndTrainerIdAndEndTimeBefore(
            LocalDateTime startTime, LocalDateTime endTime, LocalDateTime now, String trainerId);

    @Query("{ 'startTime': { $gte: ?0, $lte: ?1 }, 'endTime': { $lt: ?2 }, 'trainer.id': ?3, 'id': ?4 }")
    Training findByStartTimeBetweenAndTrainerIdAndIdAndEndTimeBefore(
            LocalDateTime startTime, LocalDateTime endTime, LocalDateTime now, String trainerId, String trainingId);

    @Query("{ 'endTime': { $lt: ?0 }, 'trainer.id': ?1, 'id': ?2 }")
    Training findByEndTimeBeforeAndTrainerIdAndId(
        LocalDateTime now, String trainerId, String trainingId);

    @Query("{ 'endTime': { $lt: ?0 }, 'trainer.id': ?1 }")
    List<Training> findAllByEndTimeBeforeAndTrainerId(
        LocalDateTime now, String trainerId);

    @Query("{ 'trainer.id': { $in: ?0 } }")
    List<Training> findAllByTrainerIds(List<String> trainerIds);

    @Query("{ 'endTime': { $lt: ?0 } }")
    List<Training> findAllByEndTimeBefore(LocalDateTime now);
            
    // Optional<Training> findByIdAndTrainerId(String id, String trainingId);

}
