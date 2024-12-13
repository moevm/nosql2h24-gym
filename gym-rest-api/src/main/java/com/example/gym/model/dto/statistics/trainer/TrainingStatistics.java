package com.example.gym.model.dto.statistics.trainer;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Trainings Statisctics", description = "Статистика по тренировокам")
public class TrainingStatistics {

    @Schema(description = "Количество проведенных тренировок", example = "20")
    private Integer count;
    @Schema(description = "Общая прибыль", example = "100000.00")
    private Double totalProfit;

    private List<ResponseTrainingForStatistics> trainigs;

}
