package com.example.gym.model.promotion.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Create Promotion", description = "Сущность для создания promotion")
public class CreatePromotionDto {

    @Schema(name = "Название")
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Schema(name = "Описание")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Schema(name = "Время начала")
    @NotNull(message = "Время начала не может быть пустым")
    @FutureOrPresent(message = "Время начала должно быть в будущем или настоящем")
    private LocalDateTime startDate;

    @Schema(name = "Время окончания")
    @NotNull(message = "Время окончания не может быть пустым")
    @Future(message = "Время окончания должно быть в будущем")
    private LocalDateTime endDate;

    @Schema(name = "Скидка в процентах")
    @NotNull(message = "Скидка в процентах не может быть пустой")
    @Min(value = 0, message = "Скидка в процентах должна быть не менее 0")
    @Max(value = 100, message = "Скидка в процентах должна быть не более 100")
    private Integer discountPercentage;
    
    @Schema(name = "Идентификатор создателя")
    @NotBlank(message = "Идентификатор создателя не может быть пустым")
    private String creatorId;

}
