package com.example.gym.model.room;

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
@Schema(name = "Location", description = "Адрес зала")
public class LocationPojo {

    @Schema(description = "Адрес")
    @NotBlank(message = "Адрес не может быть пустым")
    private String address;

    @Schema(description = "Номер")
    @NotNull(message = "Номер не может быть пустым")
    @Positive(message = "Номер комнаты должен быть положительным")
    private Integer number;

}
