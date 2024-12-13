package com.example.gym.model.promotion.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Update Promotion", description = "Сущность для изменения promotion")
public class UpdatePromotionDto {

    @Schema(name = "Название")
    private String name;

    @Schema(name = "Описание")
    private String description;

    @Schema(name = "Время начала")
    @FutureOrPresent(message = "Время начала должно быть в будущем или настоящем")
    private LocalDateTime startDate;

    @Schema(name = "Время окончания")
    @Future(message = "Время окончания должно быть в будущем")
    private LocalDateTime endDate;

    @Schema(name = "Скидка в процентах")
    @Min(value = 0, message = "Скидка в процентах должна быть не менее 0")
    @Max(value = 100, message = "Скидка в процентах должна быть не более 100")
    private Integer discountPercentage;

}
