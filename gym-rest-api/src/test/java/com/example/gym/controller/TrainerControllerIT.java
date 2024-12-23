package com.example.gym.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.gym.config.TrainingTestDataFactory;
import com.example.gym.config.UserTestDataFactory;
import com.example.gym.model.room.Room;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.trainer.update.UpdateTrainerDto;
import com.example.gym.model.trainer.update.UpdateTrainerInfo;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.GenderType;
import com.fasterxml.jackson.core.type.TypeReference;

public class TrainerControllerIT extends BaseTests {

    @Nested
    @DisplayName("Тесты поиска тренеров c фильтрами")
    public class TrainerSearchTests {
        @Test
        @DisplayName("Получение списка тренеров по подстроке названия секции")
        void searchTrainers_WithSectionNameFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainers")
                    .param("section", "Бок")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].sections", everyItem(hasItem(containsString("Бок")))))
                .andDo(result -> {
                    List<ResponseTrainerWithoutTrainingsDto> trainers = objectMapper.readValue(
                        result.getResponse().getContentAsString(StandardCharsets.UTF_8), 
                        new TypeReference<List<ResponseTrainerWithoutTrainingsDto>>() {}
                    );

                    assertTrue(trainers.stream()
                        .allMatch(trainer -> 
                            trainer.getSections().stream()
                                .anyMatch(section -> section.contains("Бок"))
                        )
                    );
                });
        }

        @Test
        @DisplayName("Получение списка тренеров по подстроке имени тренера")
        void searchTrainers_WithTrainerNameFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainers")
                    .param("name", "Ива")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[*].trainerInfo.name").value(everyItem(containsString("Ива"))));
        }

        @Test
        @DisplayName("Получение списка тренеров по подстроке фамилии тренера")
        void searchTrainers_WithTrainerSurnameFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainers")
                    .param("surname", "Иван")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].trainerInfo.surname").value(everyItem(containsString("Иван"))));
        }

        @Test
        @DisplayName("Получение списка тренеров по полу")
        void searchTrainers_WithGenderFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainers")
                    .param("gender", GenderType.FEMALE.toString())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[*].trainerInfo.gender").value(everyItem(containsString(GenderType.FEMALE.toString()))));
        }

        @Test
        @DisplayName("Получение списка тренеров по дате рождения")
        void searchTrainers_WithBirthdayFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainers")
                    .param("birthdayFrom", LocalDateTime.of(2024, 12, 01, 0, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].birthday").exists())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    List<User> trainers = objectMapper.readValue(responseBody, 
                        new TypeReference<List<User>>() {});
                    
                    assertFalse(trainers.isEmpty(), "Список тренеров не должен быть пустым");
                    
                    trainers.forEach(trainer -> {
                        assertTrue(
                            trainer.getBirthday().isAfter(LocalDateTime.of(2024, 11, 30, 0, 0, 0)) ||
                            trainer.getBirthday().isEqual(LocalDateTime.of(2024, 11, 30, 0, 0, 0))
                        );
                    });
                });

            mockMvc.perform(get("/trainers")
                    .param("birthdayBefore", LocalDateTime.of(2024, 12, 01, 0, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].birthday").exists())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    List<User> trainers = objectMapper.readValue(responseBody, 
                        new TypeReference<List<User>>() {});
                    
                    assertFalse(trainers.isEmpty(), "Список тренеров не должен быть пустым");
                    
                    trainers.forEach(trainer -> {
                        assertTrue(
                            trainer.getBirthday().isBefore(LocalDateTime.of(2024, 12, 1, 0, 0, 0)) ||
                            trainer.getBirthday().isEqual(LocalDateTime.of(2024, 12, 1, 0, 0, 0))
                        );
                    });
                });

            mockMvc.perform(get("/trainers")
                    .param("birthdayFrom", LocalDateTime.of(2024, 11, 30, 0, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .param("birthdayBefore", LocalDateTime.of(2024, 12, 01, 0, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].birthday").exists())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    List<User> trainers = objectMapper.readValue(responseBody, 
                        new TypeReference<List<User>>() {});
                    
                    assertFalse(trainers.isEmpty(), "Список тренеров не должен быть пустым");
                    
                    trainers.forEach(trainer -> {
                        assertTrue(
                            (trainer.getBirthday().isAfter(LocalDateTime.of(2024, 11, 30, 0, 0, 0)) ||
                            trainer.getBirthday().isEqual(LocalDateTime.of(2024, 11, 30, 0, 0, 0))) &&
                           (trainer.getBirthday().isBefore(LocalDateTime.of(2024, 12, 1, 0, 0, 0)) ||
                            trainer.getBirthday().isEqual(LocalDateTime.of(2024, 12, 1, 0, 0, 0)))
                        );
                    });
                });
        }

        @Test
        @DisplayName("Получение списка трениров используя все фильры одновременно")
        void searchTrainers_WithAllFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("section", "Бок");
            params.add("name", "Ива");
            params.add("surname", "Иван");
            params.add("gender", GenderType.MALE.toString());
            params.add("birthdayFrom", LocalDateTime.of(2024, 11, 30, 0, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            params.add("birthdatBefore", LocalDateTime.of(2025, 01, 1, 0, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));


            mockMvc.perform(get("/trainers")
                    .params(params)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[*].sections", everyItem(hasItem(containsString("Бок")))))
                .andDo(result -> {
                    List<ResponseTrainerWithoutTrainingsDto> trainers = objectMapper.readValue(
                        result.getResponse().getContentAsString(StandardCharsets.UTF_8), 
                        new TypeReference<List<ResponseTrainerWithoutTrainingsDto>>() {}
                    );

                    assertTrue(trainers.stream()
                        .allMatch(trainer -> 
                            trainer.getSections().stream()
                                .anyMatch(section -> section.contains("Бок"))
                        )
                    );
                })
                .andExpect(jsonPath("$[*].trainerInfo.name").value(everyItem(containsString("Ива"))))
                .andExpect(jsonPath("$[*].trainerInfo.surname").value(everyItem(containsString("Иван"))))
                .andExpect(jsonPath("$[*].trainerInfo.gender").value(everyItem(containsString(GenderType.MALE.toString()))))
                .andExpect(jsonPath("$[0].birthday").exists())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    List<User> trainers = objectMapper.readValue(responseBody, 
                        new TypeReference<List<User>>() {});
                    
                    assertFalse(trainers.isEmpty(), "Список тренеров не должен быть пустым");
                    
                    trainers.forEach(trainer -> {
                        assertTrue(
                            (trainer.getBirthday().isAfter(LocalDateTime.of(2024, 11, 30, 0, 0, 0)) ||
                            trainer.getBirthday().isEqual(LocalDateTime.of(2024, 11, 30, 0, 0, 0))) &&
                           (trainer.getBirthday().isBefore(LocalDateTime.of(2025, 01, 1, 0, 0, 0)) ||
                            trainer.getBirthday().isEqual(LocalDateTime.of(2025, 01, 1, 0, 0, 0)))
                        );
                    });
                });
        }

        @Test
        @DisplayName("Получение списка тренеров без фильров")
        void searchTrainers_WithNoFilters_ShouldReturnFilteredResults() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            mockMvc.perform(get("/trainers")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
        }

        @Test
        @DisplayName("Получение списка тренеров с неавторизованным пользователем")
        void searchTrainers_UserNotAuthorized_ShouldReturnUnauthorize() throws Exception {
            mockMvc.perform(get("/trainers")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Получение существующего тренера по идентификатору")
        void getTrainerById_ExistingTrainer_ShouldReturnTraining() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            String existingId = UserTestDataFactory.TRAINER_ID;

            mockMvc.perform(get("/trainers/{id}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value("Иван"))
                .andExpect(jsonPath("$.surname").value("Иванов"));
        }

        @Test
        @DisplayName("Получение не существующего тренера по идентификатору")
        void getTrainerById_NotExistingTrainer_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            mockMvc.perform(get("/trainers/{id}", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Тренер с id %s не найден' ".formatted(notExistingId) +
                        "}", 
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });
        }

    }

    @Nested
    @DisplayName("Обновление тренеров")
    public class UpdateTrainerTest {
        
        @Test
        @DisplayName("Обновление тренера валидными данными")
        void updateTrainer_ExistingTrainerAndValidData_ShouldReturnUpdatedTrainer() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = UserTestDataFactory.TRAINER_ID;
            User trainerBeforeUpdate = userRepository.findById(existingId).get();

            UpdateTrainerDto updateTrainerDto = UpdateTrainerDto.builder()
                    .name("NEW NAME")
                    .birthday(LocalDateTime.of(2024, 10, 12, 0, 0, 0))
                    .comment("NEW COMMENT")
                    .trainerInfo(new UpdateTrainerInfo(10, 10d, "NEW QUALIFICATION", List.of("NEW SECTIONS")))
                    .build();

            mockMvc.perform(put("/trainers/{trainerId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateTrainerDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NEW NAME"))
                .andExpect(jsonPath("$.trainerInfo.experience").value(10))
                .andExpect(jsonPath("$.trainerInfo.hourlyRate").value(10d))
                .andExpect(jsonPath("$.trainerInfo.qualification").value("NEW QUALIFICATION"))
                .andExpect(jsonPath("$.trainerInfo.sections[0]").value("NEW SECTIONS"))
                .andExpect(jsonPath("$.trainerInfo.sections.size()").value(1))
                .andExpect(jsonPath("$.birthday").value(LocalDateTime.of(2024, 10, 12, 0, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

            User trainerAfterUpdate = userRepository.findById(existingId).get();

            assertEquals(trainerAfterUpdate.getComment(), "NEW COMMENT");
            assertEquals(trainerBeforeUpdate.getSurname(), trainerAfterUpdate.getSurname());
            assertEquals(trainerBeforeUpdate.getEmail(), trainerAfterUpdate.getEmail());
            assertEquals(trainerBeforeUpdate.getPassword(), trainerAfterUpdate.getPassword());
        }

        @Test
        @DisplayName("Обновление тренера не валидными данными")
        void updateTrainer_ExistingTrainerAndInValidData_ShouldReturnBadRequest() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = UserTestDataFactory.TRAINER_ID;
            User trainerBeforeUpdate = userRepository.findById(existingId).get();

            UpdateTrainerDto updateTrainerDto = UpdateTrainerDto.builder()
                    .birthday(LocalDateTime.now().plusDays(1))
                    .phoneNumber("123123")
                    .email("asdasdasd")
                    .trainerInfo(new UpdateTrainerInfo(-10, -10d, "NEW QUALIFICATION", List.of("NEW SECTIONS")))
                    .build();

            mockMvc.perform(put("/trainers/{trainerId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateTrainerDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': {" +
                        "    'birthday': 'Дата рождения не может быть в будущем'," +
                        "    'phoneNumber': 'Неккоректный формат номера телефона'," +
                        "    'trainerInfo.hourlyRate': 'Почасовая ставка не может быть отрицательной'," +
                        "    'email': 'Некорректный формат электронной почты'," +
                        "    'trainerInfo.experience': 'Опыт работы не может быть отрицательным'" +
                        "  }" +
                        "}",
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );}
                );
                

            User trainerAfterUpdate = userRepository.findById(existingId).get();

            assertEquals(trainerAfterUpdate.getEmail(), trainerBeforeUpdate.getEmail());
            assertEquals(trainerBeforeUpdate.getPhoneNumber(), trainerAfterUpdate.getPhoneNumber());
            assertEquals(trainerBeforeUpdate.getTrainerInfo().getHourlyRate(), trainerAfterUpdate.getTrainerInfo().getHourlyRate());
            assertEquals(trainerBeforeUpdate.getTrainerInfo().getExperience(), trainerAfterUpdate.getTrainerInfo().getExperience());
        }

        @Test
        @DisplayName("Обновление не существующего тренера валидными данными")
        void updateTrainer_NotExistingTrainerAndValidData_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            UpdateTrainerDto updateTrainerDto = UpdateTrainerDto.builder()
                    .name("NEW NAME")
                    .birthday(LocalDateTime.of(2024, 10, 12, 0, 0, 0))
                    .comment("NEW COMMENT")
                    .trainerInfo(new UpdateTrainerInfo(10, 10d, "NEW QUALIFICATION", List.of("NEW SECTIONS")))
                    .build();

            mockMvc.perform(put("/trainers/{trainerId}", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateTrainerDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Тесты удаления тренера")
    class TrainingDeletionTests {
        @Test
        @DisplayName("Удаление тренера")
        void deleteTrainer_ExistingTrainer_ShouldDeleteSuccessfully() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = UserTestDataFactory.TRAINER_ID;

            Integer trainerCountBeforeDelete = userRepository.findAllByRoles("ROLE_TRAINER").size();

            mockMvc.perform(delete("/trainers/{id}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

            Integer trainerCountAfterDelete = userRepository.findAllByRoles("ROLE_TRAINER").size();

            assertEquals(trainerCountBeforeDelete-1, trainerCountAfterDelete);
        }
    }

    @Nested
    @DisplayName("Получение тренировок тренера")
    public class TrainerTrainingsTest {
    
        @Test
        @DisplayName("Получение тренировок тренера по идентификатору")
        void getTrainerTrainings_ExistingTrainer_ShouldReturnTraining() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            String existingId = UserTestDataFactory.TRAINER_ID;

            mockMvc.perform(get("/trainers/{id}/trainings", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
        }

            
        @Test
        @DisplayName("Получение тренировок не существующего тренера по идентификатору")
        void getTrainerTrainings_NotExistingTrainer_ShouldReturnTraining() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            mockMvc.perform(get("/trainers/{id}/trainings", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }
    }

    @Nested 
    @DisplayName("Создание тренировок")
    public class CreateTrainingTest {
    
        @Test
        @DisplayName("Создание тренировки по идентификатору тренера")
        void createTraining_ExistingTrainerAndValidData_ShouldReturnCreatedTraining() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = UserTestDataFactory.TRAINER_ID;

            User trainer = userRepository.findById(existingId).get();

            Integer trainingsCountBeforeCreate = trainingRepository.findAllByTrainerId(existingId).size();

            String roomId = TrainingTestDataFactory.roomId1;
            Room room = roomRepository.findById(roomId).get();

            LocalDateTime startTime = LocalDateTime.now().plusDays(1);
            LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(2);
            CreateTrainingDto createTrainingDto = CreateTrainingDto.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .availableSlots(10)
                    .roomId(roomId)
                    .section("SECTION")
                    .build();

            mockMvc.perform(post("/trainers/{trainerId}/trainings", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(createTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.room.name").value(room.getName()))
                .andExpect(jsonPath("$.room.capacity").value(room.getCapacity()))
                .andExpect(jsonPath("$.trainer.id").value(trainer.getId()))
                .andExpect(jsonPath("$.trainer.name").value(trainer.getName()))
                .andExpect(jsonPath("$.trainer.surname").value(trainer.getSurname()))
                .andExpect(jsonPath("$.trainer.hourlyRate").value(trainer.getTrainerInfo().getHourlyRate()))
                .andExpect(jsonPath("$.trainer.qualification").value(trainer.getTrainerInfo().getQualification()))
                .andExpect(jsonPath("$.section.name").value("SECTION"))
                .andExpect(jsonPath("$.startTime").value(startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.endTime").value(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

            Integer trainingsCountAfterCreate = trainingRepository.findAllByTrainerId(existingId).size();

            assertEquals(trainingsCountBeforeCreate+1, trainingsCountAfterCreate);
        }

        @Test
        @DisplayName("Создание тренировки по идентификатору тренера c невалидными данными")
        void createTraining_ExistingTrainerAndInValidData_ShouldReturnBadRequest() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = UserTestDataFactory.TRAINER_ID;

            Integer trainingsCountBeforeCreate = trainingRepository.findAllByTrainerId(existingId).size();

            LocalDateTime startTime = LocalDateTime.now().minusDays(1);
            LocalDateTime endTime = LocalDateTime.now().minusDays(1).minusHours(2);
            CreateTrainingDto createTrainingDto = CreateTrainingDto.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .availableSlots(-10)
                    .build();

            mockMvc.perform(post("/trainers/{trainerId}/trainings", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(createTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': {" +
                        "    'endTimeAfterStartTime': 'Время окончания тренировки должно быть после времени начала'," +
                        "    'section': 'Секция не может быть пустой'," +
                        "    'startTime': 'Время начала должно быть в будущем или настоящем'," +
                        "    'availableSlots': 'Количество мест должно быть положительным'," +
                        "    'endTime': 'Время окончания тренировки должно быть в будущем'," +
                        "    'roomId': 'ID комнаты не может быть пустым'" +
                        "  }" +
                        "}",
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );}
                );
                

            Integer trainingsCountAfterCreate = trainingRepository.findAllByTrainerId(existingId).size();

            assertEquals(trainingsCountBeforeCreate, trainingsCountAfterCreate);
        }

        @Test
        @DisplayName("Создание тренировкb по идентификатору тренера c пересечением тренировок")
        void createTraining_ExistingTrainerAndValidDataAndOverlapping_ShouldReturnBadRequest() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = UserTestDataFactory.TRAINER_ID;

            Integer trainingsCountBeforeCreate = trainingRepository.findAllByTrainerId(existingId).size();

            String roomId = TrainingTestDataFactory.roomId1;

            CreateTrainingDto createTrainingDto = CreateTrainingDto.builder()
                    .startTime(LocalDateTime.of(2024, 12, 16, 19, 30, 00))
                    .endTime(LocalDateTime.of(2024, 12, 16, 22, 13, 00))
                    .availableSlots(10)
                    .roomId(roomId)
                    .section("SECTION")
                    .build();

            mockMvc.perform(post("/trainers/{trainerId}/trainings", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(createTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Время новой тренировки пересекается с существующей тренировкой.' " +
                        "}",
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );}
                );
                

            Integer trainingsCountAfterCreate = trainingRepository.findAllByTrainerId(existingId).size();

            assertEquals(trainingsCountBeforeCreate, trainingsCountAfterCreate);
        }

        @Test
        @DisplayName("Создание тренировки по идентификатору не существующего тренера")
        void createTraining_NotExistingTrainerAndValidData_ShouldReturnNotFound() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            String roomId = TrainingTestDataFactory.roomId1;

            CreateTrainingDto createTrainingDto = CreateTrainingDto.builder()
                    .startTime(LocalDateTime.of(2024, 12, 16, 19, 30, 00))
                    .endTime(LocalDateTime.of(2024, 12, 16, 22, 13, 00))
                    .availableSlots(10)
                    .roomId(roomId)
                    .section("SECTION")
                    .build();

            mockMvc.perform(post("/trainers/{trainerId}/trainings", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(createTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': 'Тренер с id %s не найден' ".formatted(notExistingId) +
                        "}",
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );}
                );
        }

        @Test
        @DisplayName("Создание тренировки по идентификатору не существующего тренера c нарушением прав доступа")
        void createTraining_NotExistingTrainerAndValidDataAndUserNotIsTrainer_ShouldReturnForbidden() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("trainer");

            String existingId = UserTestDataFactory.TRAINER_ID;

            String roomId = TrainingTestDataFactory.roomId1;

            CreateTrainingDto createTrainingDto = CreateTrainingDto.builder()
                    .startTime(LocalDateTime.of(2024, 12, 16, 19, 30, 00))
                    .endTime(LocalDateTime.of(2024, 12, 16, 22, 13, 00))
                    .availableSlots(10)
                    .roomId(roomId)
                    .section("SECTION")
                    .build();

            mockMvc.perform(post("/trainers/{trainerId}/trainings", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(createTrainingDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Тестирование статистики")
    public class TrainerStatisticsTest {
        @Test
        @DisplayName("Получение статстики по прибыли по идентификатору тренера")
        public void getProfitStatistics_TrainerExists_ShouldReturnsStatistics() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            trainingRepository.deleteAll();

            trainingRepository.saveAll(TrainingTestDataFactory.getTrainingsForProfitStatistics());

            String existingId = UserTestDataFactory.TRAINER_ID;

            mockMvc.perform(get("/trainers/{id}/statistics/profit", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'profitStatistics': {" +
                        "     'this_month': 54000.0," +
                        "     'today': 54000.0," +
                        "     'this_week': 54000.0" +
                        "  }," +
                        "  'previous_months_profit': {" +
                        "     '2024-11': 9000.0," +
                        "     '2024-8': 13500.0" +
                        "  }" +
                        "}",
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );
                });
        }

        @Test
        @DisplayName("Получение статстики по тренировкам по идентификатору тренера")
        public void getTrainingsStatistics_TrainerExists_ShouldReturnsStatistics() throws Exception {
            securitySetup.setUpSecurityContext("trainer", List.of("ROLE_TRAINER"));
            String token = securitySetup.createTestToken("trainer");

            trainingRepository.deleteAll();

            trainingRepository.saveAll(TrainingTestDataFactory.getTrainingsForTrainingsStatistics());

            String existingId = UserTestDataFactory.TRAINER_ID;

            mockMvc.perform(get("/trainers/{id}/statistics/trainings", existingId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                        JSONAssert.assertEquals("""
                            [
                                {
                                    "date": "2024-11-14",
                                    "time": "20:15:00",
                                    "clientCount": 3,
                                    "profit": 13500.0,
                                    "id": "%s"
                                },
                                {
                                    "date": "2024-11-13",
                                    "time": "20:15:00",
                                    "clientCount": 4,
                                    "profit": 18000.0,
                                    "id": "%s"
                                },
                                {
                                    "date": "2024-11-03",
                                    "time": "20:15:00",
                                    "clientCount": 2,
                                    "profit": 9000.0,
                                    "id": "%s"
                                },
                                {
                                    "date": "2024-11-02",
                                    "time": "20:15:00",
                                    "clientCount": 2,
                                    "profit": 15000.0,
                                    "id": "%s"
                                },
                                {
                                    "date": "2024-08-03",
                                    "time": "20:15:00",
                                    "clientCount": 3,
                                    "profit": 13500.0,
                                    "id": "%s"
                                }
                            ]
                            """.formatted(TrainingTestDataFactory.trainingId5, 
                                    TrainingTestDataFactory.trainingId4, 
                                    TrainingTestDataFactory.trainingId3, 
                                    TrainingTestDataFactory.trainingId2, 
                                    TrainingTestDataFactory.trainingId1),
                            responseBody, 
                            JSONCompareMode.LENIENT
                        );
                    });
        }

        @Test
        @DisplayName("Получение статстики по прибыли по идентификатору тренера c нарушением прав доступа")
        public void getProfitStatistics_TrainerExistsAndUserIsNotTrainer_ShouldReturnsForbidden() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("client");

            trainingRepository.deleteAll();

            trainingRepository.saveAll(TrainingTestDataFactory.getTrainingsForProfitStatistics());

            String existingId = UserTestDataFactory.TRAINER_ID;

            mockMvc.perform(get("/trainers/{id}/statistics/profit", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Получение статстики по тренировкам по идентификатору тренера c нарушением прав доступа")
        public void getTrainigsStatistics_TrainerExistsAndUserIsNotTrainer_ShouldReturnsForbidden() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_CLIENT"));
            String token = securitySetup.createTestToken("client");

            trainingRepository.deleteAll();

            trainingRepository.saveAll(TrainingTestDataFactory.getTrainingsForTrainingsStatistics());

            String existingId = UserTestDataFactory.TRAINER_ID;

            mockMvc.perform(get("/trainers/{id}/statistics/trainigs", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }
    }

}
