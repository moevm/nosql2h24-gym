package com.example.gym.model.user.pojo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private SubscriptionStatus status;
    private Double price;

    private LocalDateTime freezeDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Integer getRestDays() {
        LocalDateTime now = LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(now, endDate);
        return (int) daysBetween;
    }

}
