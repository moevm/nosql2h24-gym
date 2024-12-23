package com.example.gym.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.example.gym.config.TrainingTestDataFactory;
import com.example.gym.config.UserTestDataFactory;
import com.example.gym.model.room.Room;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.UpdateTrainingDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.GenderType;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TrainingControllerIT extends BaseTests {

    @Nested
    @DisplayName("Тесты поиска тренировок c фильтрами")
    class TrainingSearchTests {
        @Test
        @DisplayName("Получение списка тренировок по подстроке названия секции")
        void searchTrainings_WithSectionNameFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainings")
                    .param("section", "Групповая")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].section.name").value(everyItem(containsString("Групповая"))));
        }

        @Test
        @DisplayName("Получение списка тренировок по подстроке адреса проведения тренировки")
        void searchTrainings_WithRoomAddressFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainings")
                    .param("address", "Address")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[*].room.address").value(everyItem(containsString("Address"))));
        }

        @Test
        @DisplayName("Получение списка тренировок по подстроке имени тренера")
        void searchTrainings_WithTrainerNameFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainings")
                    .param("name", "Макси")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].trainerInfo.name").value(everyItem(containsString("Макси"))));
        }

        @Test
        @DisplayName("Получение списка тренировок по подстроке фамилии тренера")
        void searchTrainings_WithTrainerSurnameFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainings")
                    .param("surname", "Федоров")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].trainerInfo.surname").value(everyItem(containsString("Федоров"))));
        }

        @Test
        @DisplayName("Получение списка тренировок по полу")
        void searchTrainings_WithGenderFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainings")
                    .param("gender", GenderType.MALE.toString())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$[*].trainerInfo.gender").value(everyItem(containsString(GenderType.MALE.toString()))));
        }

        @Test
        @DisplayName("Получение списка тренировок по дате и времени начала тренировки")
        void searchTrainings_WithTrainingStartTimeFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            LocalDateTime startTime = LocalDateTime.of(2024, 12, 18, 22, 45, 00);

            mockMvc.perform(get("/trainings")
                    .param("startTime", startTime.toString())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].startTime").exists())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    List<Training> trainings = objectMapper.readValue(responseBody, 
                        new TypeReference<List<Training>>() {});
                    
                    assertFalse(trainings.isEmpty(), "Список тренировок не должен быть пустым");
                    
                    trainings.forEach(training -> {
                        LocalDateTime trainingStart = training.getStartTime();
                        assertTrue(
                            !trainingStart.isBefore(startTime)
                        );
                    });
                });
        }

        @Test
        @DisplayName("Получение списка тренировок используя все фильры одновременно")
        void searchTrainings_WithAllFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("section", "Групповая");
            params.add("address", "Addre");
            params.add("name", "Алек");
            params.add("surname", "Иванов");
            params.add("gender", GenderType.MALE.toString());
            params.add("startTime", "2024-12-18T22:45:00");


            mockMvc.perform(get("/trainings")
                    .params(params)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[*].section.name").value(everyItem(containsString("Групповая"))))
                .andExpect(jsonPath("$[*].room.address").value(everyItem(containsString("Addre"))))
                .andExpect(jsonPath("$[*].trainerInfo.name").value(everyItem(containsString("Алек"))))
                .andExpect(jsonPath("$[*].trainerInfo.surname").value(everyItem(containsString("Иванов"))))
                .andExpect(jsonPath("$[*].trainerInfo.gender").value(everyItem(containsString(GenderType.MALE.toString()))))
                .andExpect(jsonPath("$[0].startTime").exists())
                .andDo(result -> {
                        String responseBody = result.getResponse().getContentAsString();
                        List<Training> trainings = objectMapper.readValue(responseBody, 
                                new TypeReference<List<Training>>() {});
                        
                        assertFalse(trainings.isEmpty(), "Список тренировок не должен быть пустым");
                        
                        trainings.forEach(training -> {
                                LocalDateTime trainingStart = training.getStartTime();
                                assertTrue(
                                    !trainingStart.isBefore(LocalDateTime.parse("2024-12-18T22:45:00"))
                                );
                        });
                });
        }

        @Test
        @DisplayName("Получение списка тренировок без фильров")
        void searchTrainings_WithNoFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainings")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(11));
        }

        @Test
        @DisplayName("Получение списка тренировок с неавторизованным пользователем")
        void searchTrainings_UserNotAuthorized_ShouldReturnUnauthorize() throws Exception {
            mockMvc.perform(get("/trainings")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Получение существующей тренировки по идентификатору")
        void getTrainingById_ExistingTraining_ShouldReturnTraining() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID;

            mockMvc.perform(get("/trainings/{id}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.trainer.name").value("Алексей"));
        }

        @Test
        @DisplayName("Получение не существующей тренировки по идентификатору")
        void getTrainingById_NotExistingTraining_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            mockMvc.perform(get("/trainings/{id}", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Тренировка с id %s не найдена' ".formatted(notExistingId) +
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });
        }

        @Test
        @DisplayName("Получение существующей тренировки по идентификатору c неавторизованным пользователем")
        void getTrainingById_ExistingTrainingAndUnauthorizedUser_ShouldReturnTraining() throws Exception {
            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID;

            mockMvc.perform(get("/trainings/{id}", existingId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Получение тренировок после удаления комнаты")
        void getTrainingsAfterRoomsDelete_ShouldReturnTrainings() throws Exception {
            securitySetup.setUpSecurityContext("admin", List.of("ROLE_ADMIN"));
            String token = securitySetup.createTestToken("admin");

            String existingId = TrainingTestDataFactory.roomId1;

            mockMvc.perform(delete("/rooms/{roomId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(status().isOk());

            long trainingCount = trainingRepository.count();

            mockMvc.perform(get("/trainings")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray(),
                            jsonPath("$.length()").value(trainingCount)
                    );
        }
    
    }

    @Nested
    @DisplayName("Тесты регистрации клиентов на тренировки")
    class TrainingRegistrationTests {
        @Test
        @DisplayName("Получение зарегистрированных на существующую тренировку пользователей")
        void getTrainingRegistration_ExistingTraining_ShouldReturnTrainingClients() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_CLIENTS;

            mockMvc.perform(get("/trainings/{id}/registration", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        @DisplayName("Получение зарегистрированных на не существующую тренировку пользователей")
        void getTrainingRegistration_NotExistingTraining_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            mockMvc.perform(get("/trainings/{id}/registration", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Регистрация клиента на существующую тренировку")
        void registrationClient_ExistingTraining_ShouldReturnOk() throws Exception {
            securitySetup.setUpSecurityContext("client_for_registration", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("client_for_registration");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_CLIENTS;
            String clientId = UserTestDataFactory.CLIENT_ID_FOR_REGISTRAION;

            User client = userRepository.findById(clientId).get();

            Training training = trainingRepository.findById(existingId).get();
            Integer initialNumberOfAvailableSlots = training.getAvailableSlots();

            mockMvc.perform(post("/trainings/{id}/registration/{clientId}", existingId, clientId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

            mockMvc.perform(get("/trainings/{id}/registration", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].id").value(clientId))
                .andExpect(jsonPath("$[1].name").value(client.getName()))
                .andExpect(jsonPath("$[1].surname").value(client.getSurname()))
                .andExpect(jsonPath("$[1].gender").value(client.getGender().toString()))
                .andExpect(jsonPath("$[1].loyaltyPoints").value(client.getClientInfo().getLoyaltyPoints()))
                .andExpect(jsonPath("$[1].registrationDate") .value(client.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

            Training trainingAfterRegistration = trainingRepository.findById(existingId).get();
            Integer numberOfAvailableSlotsAfterRegistration = trainingAfterRegistration.getAvailableSlots();

            assertEquals(initialNumberOfAvailableSlots-1, numberOfAvailableSlotsAfterRegistration);
        }

        @Test
        @DisplayName("Регистрация клиента на не существующую тренировку")
        void registrationClient_NotExistingTraining_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("client_for_registration", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("client_for_registration");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";
            String clientId = UserTestDataFactory.CLIENT_ID_FOR_REGISTRAION;

            mockMvc.perform(post("/trainings/{id}/registration/{clientId}", notExistingId, clientId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        }

        @Test
        @DisplayName("Регистрация не существующего клиента на существующую тренировку")
        void registrationClient_ExistingTrainingAndNotExistingUser_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("client_for_registration", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("client_for_registration");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_CLIENTS;
            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            Training training = trainingRepository.findById(existingId).get();
            Integer initialNumberOfAvailableSlots = training.getAvailableSlots();

            mockMvc.perform(post("/trainings/{id}/registration/{clientId}", existingId, notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            mockMvc.perform(get("/trainings/{id}/registration", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

            Training trainingAfterRegistration = trainingRepository.findById(existingId).get();
            Integer numberOfAvailableSlotsAfterRegistration = trainingAfterRegistration.getAvailableSlots();

            assertEquals(initialNumberOfAvailableSlots, numberOfAvailableSlotsAfterRegistration);

        }

        @Test
        @DisplayName("Повторная регистрация клиента на записанную тренировку")
        void reRegistrationClient_ExistingTraining_ShouldReturnBadRequest() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("client");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_CLIENTS;
            String clientId = UserTestDataFactory.CLIENT_ID;

            Training training = trainingRepository.findById(existingId).get();
            Integer initialNumberOfAvailableSlots = training.getAvailableSlots();

            mockMvc.perform(post("/trainings/{id}/registration/{clientId}", existingId, clientId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Клиент уже записан на эту тренировку' " +
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });

            mockMvc.perform(get("/trainings/{id}/registration", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

            
            Training trainingAfterRegistration = trainingRepository.findById(existingId).get();
            Integer numberOfAvailableSlotsAfterRegistration = trainingAfterRegistration.getAvailableSlots();

            assertEquals(initialNumberOfAvailableSlots, numberOfAvailableSlotsAfterRegistration);

        }

        @Test
        @DisplayName("Регистрация клиента на тренировку без свободных мест")
        void registrationClient_ExistingTrainingAndZeroAvailableSlots_ShouldReturnBadRequest() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("client");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_NOT_AVAILABLE_SLOTS;
            String clientId = UserTestDataFactory.CLIENT_ID;
            String clientIdForSecondRegistration = UserTestDataFactory.CLIENT_ID_FOR_REGISTRAION;

            Training training = trainingRepository.findById(existingId).get();
            Integer initialNumberOfAvailableSlots = training.getAvailableSlots();
            assertTrue(training.isFree());
            
            mockMvc.perform(post("/trainings/{id}/registration/{clientId}", existingId, clientId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

            mockMvc.perform(get("/trainings/{id}/registration", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(clientId));
            
            Training trainingAfterRegistration = trainingRepository.findById(existingId).get();
            Integer numberOfAvailableSlotsAfterRegistration = trainingAfterRegistration.getAvailableSlots();

            assertEquals(initialNumberOfAvailableSlots-1, numberOfAvailableSlotsAfterRegistration);
            assertFalse(trainingAfterRegistration.isFree());

            mockMvc.perform(post("/trainings/{id}/registration/{clientId}", existingId, clientIdForSecondRegistration)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Свободных записей нет' " +
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });

        }
    }

    @Nested
    @DisplayName("Обновление тренировок")
    public class UpdateTrainingTest {
        @Test
        @DisplayName("Обновление тренировок валидными данными")
        void updateTraining_ExistingTrainingAndValidData_ShouldReturnUpdatedTraining() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_CLIENTS;
            Training trainingBeforeUpdate = trainingRepository.findById(existingId).get();

            Room newTrainingRoom = roomRepository.findById(TrainingTestDataFactory.roomId1).get();

            LocalDateTime startTime = LocalDateTime.now().plusDays(1);
            LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(2);
            UpdateTrainingDto updateTrainingDto = UpdateTrainingDto.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .availableSlots(10)
                    .section("New section")
                    .roomId(TrainingTestDataFactory.roomId1)
                    .build();

            mockMvc.perform(put("/trainings/{trainingId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.section.name").value("New section"))
                .andExpect(jsonPath("$.room.id").value(TrainingTestDataFactory.roomId1))
                .andExpect(jsonPath("$.room.name").value(newTrainingRoom.getName()))
                .andExpect(jsonPath("$.room.capacity").value(newTrainingRoom.getCapacity()))
                .andExpect(jsonPath("$.startTime").value(startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.endTime").value(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.clients.length()").value(trainingBeforeUpdate.getClients().size()));
        }

        @Test
        @DisplayName("Обновление тренировок не валидными данными")
        void updateTraining_ExistingTrainingAndInvalidData_ShouldReturnBadRequest() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_CLIENTS;
            Training trainingBeforeUpdate = trainingRepository.findById(existingId).get();

            UpdateTrainingDto updateTrainingDto = UpdateTrainingDto.builder()
                    .startTime(LocalDateTime.of(2024, 12, 14, 12, 15, 00))
                    .endTime(LocalDateTime.of(2024, 12, 12, 12, 15, 00))
                    .availableSlots(-1)
                    .roomId(TrainingTestDataFactory.roomId1)
                    .build();

            mockMvc.perform(put("/trainings/{trainingId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': {" +
                        "    'endTimeAfterStartTime': 'Время окончания тренировки должно быть после времени начала'," +
                        "    'startTime': 'Время начала должно быть в будущем или настоящем'," +
                        "    'endTime': 'Время окончания тренировки должно быть в будущем'," +
                        "    'availableSlots': 'Количество мест должно быть положительным'" +
                        "  }" +
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );}
                );

            Training trainingAfterUpdate = trainingRepository.findById(existingId).get();

            assertEquals(trainingBeforeUpdate.getStartTime(), trainingAfterUpdate.getStartTime());
            assertEquals(trainingBeforeUpdate.getAvailableSlots(), trainingAfterUpdate.getAvailableSlots());
            assertEquals(trainingBeforeUpdate.getRoom().getId(), trainingAfterUpdate.getRoom().getId());
        }

        @Test
        @DisplayName("Обновление не существующей тренировоки валидными данными")
        void updateTraining_NotExistingTrainingAndValidData_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            LocalDateTime startTime = LocalDateTime.now().plusDays(1);
            LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(2);
            UpdateTrainingDto updateTrainingDto = UpdateTrainingDto.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .availableSlots(10)
                    .section("New section")
                    .roomId(TrainingTestDataFactory.roomId1)
                    .build();

            mockMvc.perform(put("/trainings/{trainingId}", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Тренировка с id %s не найдена' ".formatted(notExistingId) +
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });
        }

        @Test
        @DisplayName("Обновление существующей тренировоки валидными данными c нарушением прав доступа")
        void updateTraining_ExistingTrainingAndValidDataAndUnauthorizeUser_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("client");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_CLIENTS;

            UpdateTrainingDto updateTrainingDto = UpdateTrainingDto.builder()
                    .startTime(LocalDateTime.of(2024, 12, 16, 12, 15, 00))
                    .endTime(LocalDateTime.of(2024, 12, 16, 14, 15, 00))
                    .availableSlots(10)
                    .section("New section")
                    .roomId(TrainingTestDataFactory.roomId1)
                    .build();

            mockMvc.perform(put("/trainings/{trainingId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }
    }
    
    @Nested
    @DisplayName("Тесты удаления тренировок")
    class TrainingDeletionTests {
        @Test
        @DisplayName("Удаление существующей тренировки")
        void deleteTraining_ExistingTraining_ShouldDeleteSuccessfully() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String clientToken = securitySetup.createTestToken("client");

            String clinetId = UserTestDataFactory.CLIENT_ID;

            mockMvc.perform(get("/clients/{clientId}/trainings", clinetId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + clientToken)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = TrainingTestDataFactory.DEFAULT_TRAINING_ID_WITH_CLIENTS;

            mockMvc.perform(delete("/trainings/{id}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String clientTokenAfterDelete = securitySetup.createTestToken("client");

            mockMvc.perform(get("/clients/{clientId}/trainings", clinetId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + clientTokenAfterDelete)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        }
    }

}

