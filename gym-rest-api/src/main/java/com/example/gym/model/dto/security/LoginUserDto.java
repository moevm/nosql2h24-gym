package com.example.gym.model.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
    
    @Schema(description = "Электронная почта", example = "example@example.com")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Не верный формат электронной почты")
    private String email;

}
