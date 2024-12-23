package com.example.gym.model.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Schema(description = "Фамилия", example = "Иванов")
    @NotBlank(message = "Фамилия не может быть пустой")
    private String surname;

    @Schema(description = "Пароль", example = "password")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @Schema(description = "Электронная почта", example = "example@example.com")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Не верный формат электронной почты")
    private String email;

    @Schema(description = "Номер телефона", example = "+79998887766")
    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^(?=(?:\\D*\\d){11}\\D*$).*", message = "Номер телефона должен содержать 11 цифр")
    private String phoneNumber;

}
