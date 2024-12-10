package com.example.gym.model.subscription.dto;

import java.time.LocalDateTime;

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
@Schema(name = "Subscription", description = "Сущность абонемента")
public class ResponseSubscriptionDto {

    @Schema(description = "Длительность абонемента в днях", example = "30")
    private Long duration;
    @Schema(description = "Дата приобретения абонемента", example = "2002-08-15T00:00:00Z")
    private LocalDateTime startDate;
    @Schema(description = "Дата окончания абонемента", example = "2002-08-15T00:00:00Z")
    private LocalDateTime endDate;
    @Schema(description = "Идентификатор клиента", example = "1")
    private String clientId;
    @Schema(description = "Остаток дней действия абонемента", example = "15")
    private Integer restDays;
    @Schema(description = "Статус абонемента", enumAsRef = true)
    private SubscriptionStatus status;

}
