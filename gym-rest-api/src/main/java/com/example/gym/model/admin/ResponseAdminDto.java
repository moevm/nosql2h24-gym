package com.example.gym.model.admin;

import java.time.LocalDateTime;

import com.example.gym.model.user.pojo.GenderType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Admin", description = "Сущность админа")
public class ResponseAdminDto {

    @Schema(description = "Идентификатор", example = "1")
    private String id;
    @Schema(description = "Имя", example = "Иван")
    private String name;
    @Schema(description = "Фамилия", example = "Иванов")
    private String surname;
    @Schema(description = "Пароль", example = "password")
    private String password;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    @Schema(description = "Пол", enumAsRef = true)
    private GenderType gender;
    @Schema(description = "Дата рождения", example = "2002-08-15T00:00:00Z")
    private LocalDateTime birthday;
    
}
