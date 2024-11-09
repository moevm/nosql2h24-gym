package com.example.gym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gym.model.user.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    // List<User> findAllByFreeAndRoleIndex(boolean isFree, Integer roleIndex);
    List<User> findAllByRoleIndex(Integer roleIndex);

}
