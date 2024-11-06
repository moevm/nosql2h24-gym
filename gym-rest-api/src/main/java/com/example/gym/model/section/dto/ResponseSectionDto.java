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
@Schema(name = "Section", description = "Сущность секции")
public class ResponseSectionDto {

    @Schema(description = "Идентификатор", example = "1")
    private String id;
    @Schema(description = "Название", example = "Бокс")
    private String name;

}
