package com.example.gym.model.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Hourly Rate", description = "Сущность для установки почасовой ставки для тренера.")
public class HourlyRateDto {

    @Schema(description = "Почасовая ставка в рублях", example = "500")
    private Integer hourlyRate;

}
