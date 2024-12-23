package com.example.gym.model.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.model.user.pojo.TrainerInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "users")
@ToString
@Builder
public class User {

    @Id
    @JsonProperty("_id")
    private String id;

    private String name;
    private String surname;
    private String password;
    private String email;
    private String phoneNumber;
    private String comment;
    private List<String> roles;
    
    private GenderType gender;
    private LocalDateTime birthday;

    @JsonProperty("trainerInfo")
    private TrainerInfo trainerInfo;
    @JsonProperty("clientInfo")
    private ClientInfo clientInfo;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public User(String name, String surname, String email, String password, String phoneNumber, List<String> roles) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

}
