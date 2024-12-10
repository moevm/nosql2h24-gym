package com.example.gym.model.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull(message = "Длительность не может быть пустой")
    @Positive(message = "Длительность должна быть положительной")
    private Integer duration;

    @Schema(description = "Стоимость", example = "300.00")
    @NotNull(message = "Стоимость не может быть пустой")
    @Positive(message = "Стоимость должна быть положительной")
    @Digits(integer = 10, fraction = 2, message = "Стоимость должна быть в формате XXXXXXXX.XX")
    private Double price;

}
