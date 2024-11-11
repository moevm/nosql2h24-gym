package com.example.gym.model.section.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Create Section", description = "Сущность секции, для создания")
public class CreateSectionDto {

    @Schema(description = "Название", example = "Бокс")
    private String name;

}
