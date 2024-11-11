package com.example.gym.model.user.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.gym.model.subscription.SubscriptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subscription {
    
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
