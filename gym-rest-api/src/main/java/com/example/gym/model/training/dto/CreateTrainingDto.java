package com.example.gym.model.training.dto;

import java.time.LocalDateTime;

import com.example.gym.model.room.RoomPojo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Create Training", description = "Сущность тренировки, для создания")
public class CreateTrainingDto {

    @Schema(description = "Время начала тренировки в формате ISO 8601", example = "2023-10-01T15:30:00")
    private LocalDateTime startTime;
    @Schema(description = "Время окончания тренировки в формате ISO 8601", example = "2023-10-01T16:30:00")
    private LocalDateTime endTime;
    @Schema(description = "Количество мест", example = "10")
    private Integer availableSlots;
    @Schema(description = "Секция", example = "Плавание")
    private String section;
    //    @Schema(description = "ID комнаты", example = "1")
//    private String roomId;
//    private String name;
//    private String capacity;
    private RoomPojo room;

}
