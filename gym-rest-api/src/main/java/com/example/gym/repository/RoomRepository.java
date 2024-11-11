package com.example.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gym.model.room.Room;

public interface RoomRepository extends MongoRepository<Room, String> {

}
