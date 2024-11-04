package com.example.gym.model.subscription.dto;

import java.time.LocalDate;

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

    @Schema(description = "Идентификатор", example = "30")
    private String id;
    @Schema(description = "Длительность абонемента в днях", example = "30")
    private Integer duration;
    @Schema(description = "Дата приобретения абонемента", example = "2023-10-01")
    private LocalDate startDate;
    @Schema(description = "Дата окончания абонемента", example = "2023-10-01")
    private LocalDate endDate;
    @Schema(description = "Идентификатор клиента", example = "1")
    private String clientId;
    @Schema(description = "Остаток дней действия абонемента", example = "15")
    private Integer restDays;
    @Schema(description = "Статус абонемента", example = "true")
    private boolean active;

}
