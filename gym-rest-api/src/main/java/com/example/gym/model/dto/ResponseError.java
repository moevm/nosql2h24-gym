package com.example.gym.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(name = "Error", description = "Сущность ошибки")
public class ResponseError {

    @Schema(description = "Статус ошибки", example = "404")
    private Integer statusCode;
    @Schema(description = "Сообщение", example = "Ошибка при получении данных")
    private String message;

}
