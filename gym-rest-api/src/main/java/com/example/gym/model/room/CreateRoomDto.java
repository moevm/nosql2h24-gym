package com.example.gym.model.room;

import java.time.LocalTime;
import java.util.List;

import com.example.gym.model.user.pojo.Section;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Create Room", description = "Сущность для создания комнаты")
public class CreateRoomDto {

    @Schema(description = "Название зала", example = "Название")
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    
    @Schema(description = "Вместительность")
    @NotNull(message = "Вместительность не может быть пустой")
    @Positive(message = "Вместительность должна быть положительной")
    private Integer capacity;

    private LocationPojo location;

    @Schema(description = "Рабочие дни", example = "ВТ, ЧТ, СБ")
    @NotNull(message = "Рабочие дни не могут быть пустыми")
    private String workingDays;

    @Schema(description = "Часы открытия", example = "09:30:00")
    @NotNull(message = "Часы открытия не могут быть пустыми")
    private LocalTime openingTime;

    @Schema(description = "Часы закрытия", example = "16:30:00")
    @NotNull(message = "Часы закрытия не могут быть пустыми")
    private LocalTime closingTime;

    @ArraySchema(schema = @Schema(description = "Id тренеров", implementation = String.class))
    private List<String> trainers;

    @ArraySchema(schema = @Schema(description = "Секции", example = "['Name1', 'Name2']"))
    @NotNull(message = "Секции не могут быть пустыми")
    private List<String> sections;

}
