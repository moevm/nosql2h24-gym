package com.example.gym.model.trainer.update;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Update Trainer Info", description = "Информация о тренере для обновления")
public class UpdateTrainerInfo {
    
    @Schema(description = "Опыт работы", example = "2")
    @Min(value = 0, message = "Опыт работы не может быть отрицательным")
    private Integer experience;
    @Schema(description = "Почасовая ставка в рублях", example = "500")
    @Min(value = 0, message = "Почасовая ставка не может быть отрицательной")
    private Double hourlyRate;
    @Schema(description = "Специализация", example = "")
    private String qualification;
    @ArraySchema(arraySchema = @Schema(description = "Список названий секций, в которых преподает тренер", implementation = String.class))
    private List<String> sections;

}
