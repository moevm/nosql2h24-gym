package com.example.gym.model.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Login", description = "Сущность для авторизации пользователя")
public class LoginUserDto {

    @Schema(description = "Пароль", example = "password")
    private String password;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;

}
