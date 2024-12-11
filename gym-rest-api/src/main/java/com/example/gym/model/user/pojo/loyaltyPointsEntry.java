package com.example.gym.model.user.pojo;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class loyaltyPointsEntry {

    private Integer pointsChange;
    private String transactionType;
    private String description;
    private LocalDateTime transactionDate;
    
}
