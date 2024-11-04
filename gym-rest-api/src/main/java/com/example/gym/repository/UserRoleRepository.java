package com.example.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gym.model.user.UserRole;
import com.example.gym.model.user.UserRoleType;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {

    UserRole findByName(UserRoleType name);

}
