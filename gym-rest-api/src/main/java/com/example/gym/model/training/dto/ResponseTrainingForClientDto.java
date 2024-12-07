package com.example.gym.model.training.dto;

import java.time.LocalDateTime;

import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.user.pojo.Section;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Training for client", description = "Сущность тренировки с информацей о тренере")
public class ResponseTrainingForClientDto {

    @Schema(description = "Идентификатор", example = "1")
    private String id;
    @Schema(description = "Время начала тренировки в формате ISO 8601", example = "2023-10-01T15:30:00")
    private LocalDateTime startTime;
    @Schema(description = "Время окончания тренировки в формате ISO 8601", example = "2023-10-01T15:30:00")
    private LocalDateTime endTime;
    @Schema(description = "Длительность тренировки в часах", example = "1.5")
    private Long duration;
    @Schema(description = "Секция", implementation = Section.class)
    private Section section;
    @Schema(description = "Зал", implementation = RoomPojo.class)
    private RoomPojo room;
    @Schema(description = "Тренер", implementation = ResponseTrainerWithoutTrainingsDto.class)
    private ResponseTrainerWithoutTrainingsDto trainer;

}
