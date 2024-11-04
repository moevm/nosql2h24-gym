package com.example.gym.model.trainer;

import java.util.List;

import com.example.gym.model.training.dto.ResponseTrainingDto;

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
@Schema(name = "Trainer", description = "Сущность тренера")
public class ResponseTrainerDto {
    @Schema(description = "Идентификатор", example = "1")
    private String id;
    @Schema(description = "Имя", example = "Иванов Иван Иванович")
    private String username;
    @Schema(description = "Пароль", example = "password")
    private String password;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    @Schema(description = "Номер телефона", example = "+79998887766")
    private String phoneNumber;
    @Schema(description = "Опыт работы", example = "2")
    private Integer experience;
    @Schema(description = "Почасовая ставка в рублях", example = "500")
    private Integer hourlyRate;
    @Schema(description = "Специализация", example = "")
    private String specialization;
    @Schema(description = "Список названий секций, в которых преподает тренер", example = "['Name1', 'Name2']")
    private List<String> sections;
    @ArraySchema(
        schema = @Schema(description = "Тренировка", implementation = ResponseTrainingDto.class)
    )
    private List<ResponseTrainingDto> trainings;
    @Schema(description = "Есть ли свободные тренировки?", example = "true")
    private boolean free;

}
