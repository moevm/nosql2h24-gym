package com.example.gym.model.dto.statistics.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "SectionStatistics", description = "Статистика по секции")
public class SectionStatistics {

    @Schema(description = "Название секции", example = "Фитнес")
    private String sectionName;

    @Schema(description = "Количество проведенных тренировок", example = "10")
    private Integer trainingCount;

    @Schema(description = "Количество клиентов", example = "25")
    private Integer clientCount;

    @Schema(description = "Загруженность", example = "75")
    private Double loadPercentage;

}
