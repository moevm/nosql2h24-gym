package com.example.gym.model.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.TrainerInfo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "my_users")
@ToString
public class User {

    @Id
    private String id;

    private String name;
    private String surname;
    private String password;
    private String email;
    private String phoneNumber;
    private String comment;
    private List<String> roles; 
    private Integer roleIndex;

    private TrainerInfo trainerInfo;
    private ClientInfo clientInfo;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public User(String name, String surname, String email, String password, String phoneNumber, List<String> roles, Integer roleIndex) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
        this.roleIndex = roleIndex;
    }

}
