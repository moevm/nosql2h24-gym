package com.example.gym.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;

import com.example.gym.model.dto.security.JwtResponse;
import com.example.gym.model.dto.security.LoginUserDto;
import com.example.gym.model.dto.security.RegisterUserDto;
import com.example.gym.model.user.User;

public class AuthenticationControllerIT extends BaseTests {

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void registerUser_ValidPayload_ShouldRegisterSuccessfully() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto(
            "User", 
            "Test", 
            "12345", 
            "testemail@gmail.com", 
            "+79999999999"
        );

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isOk());

        Optional<User> savedUser = userRepository.findByEmail(registerUserDto.getEmail());
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getEmail()).isEqualTo(registerUserDto.getEmail());
    }

    @Test
    @DisplayName("Регистрация с существующим email - должен вернуть ошибку")
    void registerUser_DuplicateEmail_ShouldReturnConflict() throws Exception {

        String exisitingEmail = "existing@example.com";

        User existingUser = new User();
        existingUser.setEmail(exisitingEmail);
        existingUser.setPassword("password123");
        userRepository.save(existingUser);

        long initialUserCount = userRepository.count();

        RegisterUserDto registerUserDto = new RegisterUserDto(
            "Test", 
            "User", 
            "password123", 
            exisitingEmail, 
            "+79999999999"
        );

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Пользователь с электронной почтой " + exisitingEmail +  " уже существует'" + 
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });

        long userCountAfterFailedRegistration = userRepository.count();
        assertEquals(initialUserCount, userCountAfterFailedRegistration);
    }

    @Test
    @DisplayName("POST /register c невалидной полезной нагрузкой возвращает HTTP-ответ со статусом 400 BAD REQUEST")
    void registerUser_PayloadIsInValid_ReturnsBadRequestResponseEntity() throws Exception {

        long initialUserCount = userRepository.count();

        RegisterUserDto registerUserDto = new RegisterUserDto(
            "",
            null,
            "",
            "invalid",
            "123" 
        );

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpectAll(
                    status().isBadRequest(),
                    content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        
        responseBody = responseBody.replaceAll(",\"timestamp\".*\\}$", "}");

        JSONAssert.assertEquals(
            "{" +
            "  'errors': {" +
            "    'name': 'Имя не может быть пустым'," +
            "    'surname': 'Фамилия не может быть пустой'," +
            "    'password': 'Пароль не может быть пустым'," +
            "    'email': 'Не верный формат электронной почты'," +
            "    'phoneNumber': 'Номер телефона должен содержать 11 цифр'" +
            "  }" +
            "}", 
            responseBody, 
            JSONCompareMode.LENIENT
        );

        long userCountBeforeTest = userRepository.count();
        assertEquals(initialUserCount, userCountBeforeTest);
    }

    @Test
    @DisplayName("Логин с валидными credentials")
    void login_ValidCredentials_ShouldReturnTokens() throws Exception {
        // Предварительная регистрация пользователя
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("correctPassword");
        user.setName("Test");
        user.setSurname("User");
        user.setRoles(List.of("ROLE_USER"));
        userRepository.save(user);

        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@example.com");
        loginUserDto.setPassword("correctPassword");

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(responseBody, JwtResponse.class);
        
        assertThat(jwtResponse.getAccessToken()).isNotNull();
        assertThat(jwtResponse.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    void login_InvalidPassword_ShouldReturnForbidden() throws Exception {
        // Предварительная регистрация пользователя
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("correctPassword");
        user.setName("Test");
        user.setSurname("User");
        user.setRoles(List.of("ROLE_USER"));
        userRepository.save(user);

        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@example.com");
        loginUserDto.setPassword("incorrectPassword");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isForbidden())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Неверные логин или пароль' " +
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });
    }

    @Test
    @DisplayName("Логин с несуществующим email")
    void login_NonExistentEmail_ShouldReturnForbidden() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("nonexistent@example.com");
        loginUserDto.setPassword("somePassword");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isForbidden())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Неверные логин или пароль' " +
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });
    }

}
