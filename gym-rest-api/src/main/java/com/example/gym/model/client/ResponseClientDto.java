package com.example.gym.model.client;

import java.time.LocalDateTime;
import java.util.List;

import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.GenderType;

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

    @Schema(description = "Пол", enumAsRef = true)
    private GenderType gender;
    @Schema(description = "Дата рождения", example = "2002-08-15T00:00:00Z")
    private LocalDateTime birthday;

    @ArraySchema(
            schema = @Schema(description = "Роль", example = "ROLE_USER")
    )
    private List<String> roles;

    @Schema(description = "Информация о клиенте", implementation = ClientInfo.class)
    private ClientInfo clientInfo;

    @Schema(description = "Дата создания")
    private LocalDateTime createdDate;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedDate;


}
