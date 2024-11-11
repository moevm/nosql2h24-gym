package com.example.gym.model.client;

import java.time.LocalDateTime;

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
    private Integer loyaltyPoints;
    private LocalDateTime registrationDate;
    
}
