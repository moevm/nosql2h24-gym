package com.example.gym.model.settings.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Create Loyalty Settings", description = "Сущность для настройки зачисления бонусов")
public class CreateLoyaltySettingsDto {

    @Schema(name = "Доля зачисления бонусов", example = "0.25")
    private Double acceptanceRate;

}
