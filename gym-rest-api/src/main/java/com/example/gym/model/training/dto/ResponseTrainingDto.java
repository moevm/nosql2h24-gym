package com.example.gym.model.training.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.trainer.TrainerPojo;
import com.example.gym.model.training.TrainingStatus;
import com.example.gym.model.user.pojo.Section;

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
@Schema(name = "Training", description = "Сущность тренировки")
public class ResponseTrainingDto {

    @Schema(description = "Идентификатор", example = "1")
    private String id;

    @Schema(description = "Тренер", implementation = TrainerPojo.class)
    private TrainerPojo trainer;

    @Schema(description = "Секция", implementation = Section.class)
    private Section section;

    @Schema(description = "Зал", implementation = RoomPojo.class)
    private RoomPojo room;

    @Schema(description = "Время начала тренировки в формате ISO 8601", example = "15:30:00")
    private LocalDateTime startTime;

    @Schema(description = "Время окончания тренировки в формате ISO 8601", example = "16:30:00")
    private LocalDateTime endTime;

    @Schema(description = "Количество мест", example = "3")
    private Integer availableSlots;

    @Schema(description = "Есть ли свободная запись на тренировку?", example = "true")
    private boolean hasFreeRegistration;

    @Schema(description = "Статус тренировки", enumAsRef = true)
    private TrainingStatus status;

    @ArraySchema(
            schema = @Schema(name = "Клиенты", implementation = ClientPojo.class)
    )
    @Schema(description = "Дата созадния")
    private List<ClientPojo> clients;

    @Schema(description = "Дата созадния")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedAt;

}
