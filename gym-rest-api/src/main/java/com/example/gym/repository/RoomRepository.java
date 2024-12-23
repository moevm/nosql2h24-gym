package com.example.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.gym.model.room.Room;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {

}
