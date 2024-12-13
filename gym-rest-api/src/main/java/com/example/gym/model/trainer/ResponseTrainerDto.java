package com.example.gym.model.trainer;

import java.time.LocalDateTime;
import java.util.List;

import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.model.user.pojo.TrainerInfo;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Trainer", description = "Сущность тренера")
public class ResponseTrainerDto {
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

    @Schema(description = "Номер телефона", example = "+79998887766")
    private String phoneNumber;

    @Schema(description = "Заметка")
    private String comment;

    @ArraySchema(
            schema = @Schema(description = "Роль", example = "ROLE_USER")
    )
    private List<String> roles; 
    
    private TrainerInfo trainerInfo;

    @Schema(description = "Пол", enumAsRef = true)
    private GenderType gender;
    @Schema(description = "Дата рождения", example = "2002-08-15T00:00:00Z")
    private LocalDateTime birthday;

    @Schema(description = "Дата создания")
    private LocalDateTime createdDate;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedDate;

}
