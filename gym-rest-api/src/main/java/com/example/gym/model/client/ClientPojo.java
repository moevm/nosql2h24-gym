package com.example.gym.model.client;

import java.time.LocalDateTime;

import com.example.gym.model.user.pojo.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
    public class ClientPojo {

        private String id;
        private String name;
        private String surname;
        private GenderType gender;
        private Integer loyaltyPoints;
        private LocalDateTime registrationDate;
        
    }
