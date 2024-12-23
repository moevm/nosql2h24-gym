package com.example.gym.model.promotion.dto;

import java.time.LocalDateTime;

import com.example.gym.model.promotion.CreatedBy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Promotion", description = "Сущность promotion")
public class ResponsePromotionDto {

    @Schema(name = "Идентификатор")
    private String id;

    @Schema(name = "Название")
    private String name;

    @Schema(name = "Описание")
    private String description;

    @Schema(name = "Время начала")
    private LocalDateTime startDate;

    @Schema(name = "Время окончания")
    private LocalDateTime endDate;

    @Schema(name = "Скидка в процентах")
    private Integer discountPercentage;
    
    @Schema(name = "Идентификатор создателя", implementation = CreatedBy.class)
    private CreatedBy createdBy;
}
