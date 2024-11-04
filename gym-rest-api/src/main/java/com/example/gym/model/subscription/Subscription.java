package com.example.gym.model.subscription;

import com.example.gym.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "subscriptions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subscription {

    @Id
    private String id;

    @DBRef(lazy = false)
    private User client;

    private Integer duration;

    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus active;
    private Double price;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Integer getRestDays() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

}   
