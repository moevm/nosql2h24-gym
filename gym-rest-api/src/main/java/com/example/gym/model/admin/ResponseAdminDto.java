package com.example.gym.model.admin;

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
    @Schema(description = "Имя", example = "Иванов Иван Иванович")
    private String username;
    @Schema(description = "Пароль", example = "password")
    private String password;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    
}
