package com.example.gym.repository;

import com.example.gym.model.room.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String> {

}
