package com.example.gym.model.dto.statistics.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.subscription.SubscriptionStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Subscription Detail", description = "Статистика по абонементу")
public class SubscriptionDetailDto {

    private ClientPojo client;
    @Schema(description = "Дата покупки", example = "2023-10-01")
    private LocalDateTime startDate;
    @Schema(description = "Дата окончания", example = "2023-12-01")
    private LocalDateTime endDate;
    @Schema(description = "Статус", enumAsRef = true)
    private SubscriptionStatus status;

}
