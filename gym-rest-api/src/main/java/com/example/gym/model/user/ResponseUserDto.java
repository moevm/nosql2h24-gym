package com.example.gym.model.user;

import java.time.LocalDateTime;
import java.util.List;

import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.model.user.pojo.TrainerInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseUserDto {

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
    private TrainerInfo trainerInfo;
    private ClientInfo clientInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
