package com.example.gym.model.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Update Client", description = "Сущность клиента, для обновления")
public class UpdateClientDto {

    @Schema(description = "Имя", example = "Иван")
    private String name;
    @Schema(description = "Фамилия", example = "Иванов")
    private String surname;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    @Schema(description = "Номер телефона", example = "+79998887766")
    private String phoneNumber;
    
}
