package com.example.gym.model.training.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(name = "Create Training", description = "Сущность тренировки, для создания")
public class CreateTrainingDto {

    @Schema(description = "Время начала тренировки в формате ISO 8601", example = "2023-10-01T15:30:00")
    @FutureOrPresent(message = "Время начала должно быть в будущем или настоящем")
    @NotNull(message = "Время начала не может быть пустым")
    private LocalDateTime startTime;

    @Schema(description = "Время окончания тренировки в формате ISO 8601", example = "2023-10-01T16:30:00")
    @Future(message = "Время окончания тренировки должно быть в будущем")
    @NotNull(message = "Время окончания не может быть пустым")
    private LocalDateTime endTime;

    @Schema(description = "Количество мест", example = "10")
    @NotNull(message = "Количество мест не может быть пустым")
    @Positive(message = "Количество мест должно быть положительным")
    private Integer availableSlots;

    @Schema(description = "Секция", example = "Плавание")
    @NotBlank(message = "Секция не может быть пустой")
    private String section;

    @Schema(description = "ID комнаты", example = "1")
    @NotBlank(message = "ID комнаты не может быть пустым")
    private String roomId;

    @AssertTrue(message = "Время окончания тренировки должно быть после времени начала")
    public boolean isEndTimeAfterStartTime() {
        return endTime.isAfter(startTime);
    }

}