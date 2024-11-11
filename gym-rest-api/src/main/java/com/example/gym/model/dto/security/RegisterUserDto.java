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
@Schema(name = "Register", description = "Сущность для создания пользователя")
public class RegisterUserDto {

    @Schema(description = "Имя", example = "Иван")
    private String name;
    @Schema(description = "Фамилия", example = "Иванов")
    private String surname;
    @Schema(description = "Пароль", example = "password")
    private String password;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    @Schema(description = "Номер телефона", example = "+79998887766")
    private String phoneNumber;

}
