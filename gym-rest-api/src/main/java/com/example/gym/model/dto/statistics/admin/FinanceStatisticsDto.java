package com.example.gym.model.dto.statistics.admin;

import com.example.gym.model.trainer.TrainerPojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinanceStatisticsDto {

    private TrainerPojo trainer;
    private Double profit;
    
}
