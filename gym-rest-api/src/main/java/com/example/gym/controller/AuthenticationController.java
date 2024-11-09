package com.example.gym.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.dto.security.JwtResponse;
import com.example.gym.model.dto.security.LoginUserDto;
import com.example.gym.model.dto.security.RegisterUserDto;
import com.example.gym.service.AuthenticationService;
import com.example.gym.util.ResponseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final ResponseService responseService;
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
            RegisterUserDto registerUserDto
    ) {
        try {
            authenticationService.register(registerUserDto);
            return ResponseEntity.ok().body(null);
        } catch (IllegalArgumentException exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        }
    } 

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Parameter(description = "Сущность пользователя для входа", required = true)
            LoginUserDto loginUserDto
    ) {
        try {
            JwtResponse tokens = authenticationService.login(loginUserDto);
            return ResponseEntity.ok().body(tokens);
        } catch (IllegalArgumentException exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        }  catch (Exception exception) {
            return responseService.getBadRequestResponseEntity("Некорректные данные");
        }
    }

}
