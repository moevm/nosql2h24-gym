package com.example.gym.model.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Update Subscription", description = "Сущность абонемента для обновления")
public class UpdateSubscriptionDto {

    @Schema(description = "Длительность абонемента в днях", example = "30")
    @Positive(message = "Длительность должна быть положительной")
    private Integer duration;

    @Schema(description = "Стоимость", example = "300.00")
    @Positive(message = "Стоимость должна быть положительной")
    @Digits(integer = 10, fraction = 2, message = "Стоимость должна быть в формате XXXXXXXX.XX")
    private Double price;

}
