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
@Schema(name = "Client", description = "Сущность клиента")
public class ResponseClientDto {

    @Schema(description = "Идентификатор", example = "1")
    private String id;
    @Schema(description = "Имя", example = "Иванов Иван Иванович")
    private String username;
    @Schema(description = "Пароль", example = "password")
    private String password;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    @Schema(description = "Номер телефона", example = "+79998887766")
    private String phoneNumber;
    
}
