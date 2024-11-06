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
@Schema(name = "JWT", description = "Сущность для передачи jwt-токенов на клиент")
public class JwtResponse {

    @Schema(name = "accessToken", description = "Токен доступа")
    private String accessToken;
    @Schema(name = "refreshToken", description = "Токен для обновления токена доступа")
    private String refreshToken;

}
