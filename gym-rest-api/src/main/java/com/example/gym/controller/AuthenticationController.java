package com.example.gym.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.exception.AuthenticationException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.dto.security.JwtResponse;
import com.example.gym.model.dto.security.LoginUserDto;
import com.example.gym.model.dto.security.RegisterUserDto;
import com.example.gym.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя.",
            description = "Регистрация пользователя.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно зарегистрировался.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseAdminDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> registration(
            @RequestBody @Parameter(description = "Сущность пользователя для создания", required = true)
            @Valid RegisterUserDto registerUserDto
    ) throws UniquenessViolationException {
        authenticationService.register(registerUserDto);
        return ResponseEntity.ok().body(null);
    } 

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Parameter(description = "Сущность пользователя для входа", required = true)
            @Valid LoginUserDto loginUserDto
    ) throws AuthenticationException {
        JwtResponse tokens = authenticationService.login(loginUserDto);
        return ResponseEntity.ok().body(tokens);
    }

}
