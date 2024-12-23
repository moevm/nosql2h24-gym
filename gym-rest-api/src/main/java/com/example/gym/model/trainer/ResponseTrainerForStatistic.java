package com.example.gym.model.trainer;

import java.util.List;

import com.example.gym.model.training.dto.ResponseTrainingDto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Trainer for statistic", description = "Сущность тренера для статистики")
public class ResponseTrainerForStatistic {

    @Schema(description = "Тренер", implementation = ResponseTrainerDto.class)
    private ResponseTrainerDto trainer;
    @Schema(description = "Прибыль за все проведенные тренировки", example = "10000.00")
    private Float profit;
    @Schema(description = "Количество проведенных тренировок", example = "10")
    private Integer count;
    @ArraySchema(
        schema = @Schema(description = "Тренировки", implementation = ResponseTrainingDto.class)
    )
    private List<ResponseTrainingDto> trainings;

}
