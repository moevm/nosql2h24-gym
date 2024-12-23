package com.example.gym.model.training.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Update Training", description = "Сущность тренировки, для изменения")
@Builder
public class UpdateTrainingDto {

    @Schema(description = "Время начала тренировки в формате ISO 8601", example = "2023-10-01T15:30:00")
    @FutureOrPresent(message = "Время начала должно быть в будущем или настоящем")
    private LocalDateTime startTime;

    @Schema(description = "Время окончания тренировки в формате ISO 8601", example = "2023-10-01T16:30:00")
    @Future(message = "Время окончания тренировки должно быть в будущем")
    private LocalDateTime endTime;

    @Schema(description = "Количество мест", example = "10")
    @Positive(message = "Количество мест должно быть положительным")
    private Integer availableSlots;

    @Schema(description = "Секция", example = "Плавание")
    private String section;

    @Schema(description = "ID комнаты", example = "1")
    private String roomId;

    @AssertTrue(message = "Время окончания тренировки должно быть после времени начала")
    public boolean isEndTimeAfterStartTime() {
        if (startTime != null && endTime != null) {
            return endTime.isAfter(startTime);
        }

        return true;
    }

}
