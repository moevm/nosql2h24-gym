package com.example.gym.model.client;

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
@Schema(name = "Client for statistic", description = "Сущность клиента для статистики")
public class ResponseClientForStatistic {

    @Schema(description = "Клиент", implementation = ResponseClientDto.class)
    private ResponseClientDto client;
    @Schema(description = "Количество посещенных тренировок", example = "10")
    private Integer count;
    @ArraySchema(
            schema = @Schema(description = "Тренировка", implementation = ResponseTrainingDto.class)
    )
    private List<ResponseTrainingDto> trainings;
}
