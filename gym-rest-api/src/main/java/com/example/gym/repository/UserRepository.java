package com.example.gym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.gym.model.user.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'email': ?0}")
    Optional<User> findByEmail(String email);
    @Query("{'phoneNumber': ?0}")
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findAllByRoles(String role);

}
