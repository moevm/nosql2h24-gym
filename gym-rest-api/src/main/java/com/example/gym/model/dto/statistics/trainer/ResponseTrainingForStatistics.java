package com.example.gym.model.dto.statistics.trainer;

import com.example.gym.model.training.dto.ResponseTrainingDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Training Statisctics", description = "Статистика одной тренировки")
public class ResponseTrainingForStatistics {

    @Schema(description = "Прибыль с одной тренировки", example = "1000.00")
    private Double profit;
    private ResponseTrainingDto training;

}
