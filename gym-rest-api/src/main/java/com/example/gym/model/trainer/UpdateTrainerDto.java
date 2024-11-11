package com.example.gym.model.trainer;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Update Trainer", description = "Сущность тренера, для обновления")
public class UpdateTrainerDto {

    @Schema(description = "Имя", example = "Иван")
    private String name;
    @Schema(description = "Фамилия", example = "Иванов")
    private String surname;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    @Schema(description = "Номер телефона", example = "+79998887766")
    private String phoneNumber;
    @Schema(description = "Опыт работы", example = "2")
    private Integer experience;
    @Schema(description = "Почасовая ставка в рублях", example = "500")
    private Double hourlyRate;
    @Schema(description = "Специализация", example = "")
    private String specialization;
    @Schema(description = "Список названий секций, в которых преподает тренер", example = "['Name1', 'Name2']")
    private List<String> sections;

}
