package com.example.gym.model.dto.statistics.admin;

import java.util.List;

import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;

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
@Schema(name = "Purchased Subcriptions", description = "Статистика купленных абониментов")
public class PurchasedSubcriptions {

    @Schema(description = "Количество купленных абониментов", example = "10")
    private Integer count;

    @Schema(description = "Количество активных абониментов", example = "7")
    private Integer activeCount;

    @Schema(description = "Количество замороженных абониментов", example = "3")
    private Integer freezeCount;

    @Schema(description = "Общая стоимость купленных абониментов", example = "30000")
    private Double totalPrice;

    @ArraySchema(
        schema = @Schema(description = "Абонимент", implementation = ResponseSubscriptionDto.class)
    )
    private List<SubscriptionDetailDto> subscriptions;

}
