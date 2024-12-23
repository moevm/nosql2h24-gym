package com.example.gym.model.dto.statistics.trainer;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Training Detail", description = "Статистика по тренировке")
public class TrainingDetailDto {

    @Schema(description = "Дата тренировки", example = "2023-10-01")
    private LocalDate date;

    @Schema(description = "Время начала тренировки", example = "14:30")
    private LocalTime time;

    @Schema(description = "Количество клиентов на тренировке", example = "10")
    private Integer clientCount;

    @Schema(description = "Прибыль от тренировки", example = "1500.0")
    private Double profit;

    @Schema(description = "Id тренировки", example = "653ef3a8a3e34567bcdf5001")
    private String id;

}
