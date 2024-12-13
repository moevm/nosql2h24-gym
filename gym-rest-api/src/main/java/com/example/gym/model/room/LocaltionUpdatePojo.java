package com.example.gym.model.room;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Update Location", description = "Адрес зала")
public class LocaltionUpdatePojo {

    @Schema(description = "Адрес")
    private String address;

    @Schema(description = "Номер")
    @Positive(message = "Номер комнаты должен быть положительным")
    private Integer number;

}
