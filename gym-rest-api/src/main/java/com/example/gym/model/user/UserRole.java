package com.example.gym.model.user;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "roles")
@Getter
@Setter
@NoArgsConstructor
public class UserRole {

    @Id
    private String id;

    private UserRoleType name;

    public UserRole(UserRoleType name) {
        this.name = name;
    }

}

