package com.example.gym.model.dto.statistics.admin;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Application Statistics", description = "Статистика приложения")
public class ApplicationStatistics {

    @Schema(description = "Завершенные тренировки")
    private List<SectionStatistics> trainings;

    @Schema(description = "Купленные абонименты")
    private PurchasedSubcriptions purchasedSubcriptions;

    @Schema(description = "Активность зала")
    private RoomsActive roomsActive;

}
