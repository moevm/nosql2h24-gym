package com.example.gym.model.admin;

import java.time.LocalDateTime;

import com.example.gym.model.user.pojo.GenderType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Update Admin", description = "Сущность админа, для обновления")
public class UpdateAdminDto {

    @Schema(description = "Имя", example = "Иван")
    private String name;

    @Schema(description = "Фамилия", example = "Иванов")
    private String surname;

    @Schema(description = "Электронная почта", example = "example@example.com")
    @Email(message = "Неверный формат электронной почты")
    private String email;

    @Schema(description = "Пол", enumAsRef = true)
    private GenderType gender;

    @Schema(description = "Дата рождения", example = "2002-08-15T00:00:00Z")
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDateTime birthday;
    
}
