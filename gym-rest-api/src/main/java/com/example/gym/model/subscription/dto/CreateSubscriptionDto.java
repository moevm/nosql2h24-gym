package com.example.gym.model.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Create Subscription", description = "Сущность абонемента для создания")
public class CreateSubscriptionDto {

    @Schema(description = "Длительность", example = "30")
    private Integer duration;

    @Schema(description = "Стоимость", example = "300")
    private Double price;

}
