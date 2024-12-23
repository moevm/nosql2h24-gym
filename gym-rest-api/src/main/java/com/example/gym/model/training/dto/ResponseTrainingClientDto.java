package com.example.gym.model.training.dto;

import java.util.List;

import com.example.gym.model.client.ResponseClientDto;

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
@Schema(name = "Training Client", description = "Сущность тренировок и клиентов")
public class ResponseTrainingClientDto {

    @Schema(description = "Тренировка", implementation = ResponseTrainingDto.class)
    private ResponseTrainingDto training;
    @ArraySchema(
        schema = @Schema(description = "Клиенты", implementation = ResponseClientDto.class)
    )
    private List<ResponseClientDto> clients;
}
