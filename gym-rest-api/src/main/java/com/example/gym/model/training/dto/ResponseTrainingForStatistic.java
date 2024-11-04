package com.example.gym.model.training.dto;

import java.util.List;

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
@Schema(name = "Trainings for statistic", description = "Сущность тренировок для статистики")
public class ResponseTrainingForStatistic {

    @Schema(description = "Количество проведенных тренировок", example = "10")
    private Integer count;
    @ArraySchema(
        schema = @Schema(description = "Тренировка", implementation = ResponseTrainingDto.class)
    )
    private List<ResponseTrainingDto> trainings;

}
