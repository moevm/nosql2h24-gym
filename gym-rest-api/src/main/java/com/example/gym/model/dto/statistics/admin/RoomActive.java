package com.example.gym.model.dto.statistics.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Room Active", description = "Активность зала")
public class RoomActive {

    @Schema(description = "Идентификатор зала", example = "653a1f2c1c9d440000a1bc26")
    private String id;

    @Schema(description = "Название зала", example = "Зал")
    private String name;

    @Schema(description = "Вместимость зала", example = "Зал")
    private Integer capacity;

    @Schema(description = "Количество запланированных тренировок", example = "20")
    private Long trainingCount;

    @Schema(description = "Загруженность в процентах", example = "20")
    private Double loadPercentage;

}
