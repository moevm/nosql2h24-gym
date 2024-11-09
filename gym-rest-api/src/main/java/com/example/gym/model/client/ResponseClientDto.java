package com.example.gym.model.client;

import java.time.LocalDateTime;
import java.util.List;

import com.example.gym.model.user.pojo.ClientInfo;

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
@Schema(name = "Client", description = "Сущность клиента")
public class ResponseClientDto {

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

    @Schema(description = "Индекс роли", example = "1")
    private Integer roleIndex;

    @Schema(description = "Информация о клиенте", implementation = ClientInfo.class)
    private ClientInfo clientInfo;

    @Schema(description = "Дата создания")
    private LocalDateTime createdDate;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedDate;

    
}
