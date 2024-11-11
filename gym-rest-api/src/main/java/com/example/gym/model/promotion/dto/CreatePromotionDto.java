package com.example.gym.model.promotion.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String name;

    @Schema(name = "Описание")
    private String description;

    @Schema(name = "Время начала")
    private LocalDateTime startDate;

    @Schema(name = "Время окончания")
    private LocalDateTime endDate;

    @Schema(name = "Скидка в процентах")
    private Integer discountPersentage;
    
    @Schema(name = "Идентификатор создателя")
    private String creatorId;

}
