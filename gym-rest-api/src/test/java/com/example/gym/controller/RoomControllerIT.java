package com.example.gym.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.example.gym.config.TrainingTestDataFactory;
import com.example.gym.config.UserTestDataFactory;
import com.example.gym.model.room.CreateRoomDto;
import com.example.gym.model.room.LocaltionUpdatePojo;
import com.example.gym.model.room.LocationPojo;
import com.example.gym.model.room.Room;
import com.example.gym.model.room.UpdateRoomDto;
import com.example.gym.model.training.Training;
import com.example.gym.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public class RoomControllerIT extends BaseTests {

    @Nested
    @DisplayName("Создание залов")
    public class CreateRoomTests {
        
        @Test
        @DisplayName("Создание зала")
        void createRoom_ValidData_ShouldReturnCreatedRoom() throws Exception {
            securitySetup.setUpSecurityContext("admin", List.of("ROLE_ADMIN"));
            String token = securitySetup.createTestToken("admin");

            long initialRoomCount = roomRepository.count();

            Integer roomCapacity = 50;
            String address = "Moscow";
            Integer addressNumber = 5;
            List<String> sections = List.of("Бокс", "Танцы");
            List<String> trainerIds = userRepository.findAllByRoles("ROLE_TRAINER").stream()
                    .filter(u -> u.getRoles().contains("ROLE_TRAINER"))
                    .map(User::getId)
                    .toList();
            String roomName = "New Training Room";
            LocalTime openingTime = LocalTime.of(9, 0, 0);
            LocalTime closingTime = LocalTime.of(21, 0, 0);
            String workingDays = "ПН, СР, ПТ";

            CreateRoomDto createRoomDto = CreateRoomDto.builder()
                    .capacity(roomCapacity)
                    .location(new LocationPojo(address, addressNumber))
                    .openingTime(openingTime)
                    .closingTime(closingTime)
                    .sections(sections)
                    .trainers(trainerIds)
                    .workingDays(workingDays)
                    .name(roomName)
                    .build();

            mockMvc.perform(post("/rooms")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(createRoomDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.name").value(roomName),
                        jsonPath("$.capacity").value(roomCapacity),
                        jsonPath("$.location.address").value(address),
                        jsonPath("$.location.number").value(addressNumber),
                        jsonPath("$.openingTime").value(openingTime.format(DateTimeFormatter.ISO_LOCAL_TIME)),
                        jsonPath("$.closingTime").value(closingTime.format(DateTimeFormatter.ISO_LOCAL_TIME)),
                        jsonPath("$.workingDays").value(workingDays),
                        jsonPath("$.sections", equalTo(sections)),
                        jsonPath("$.trainers", equalTo(trainerIds))
                );

            long roomCountAfterCreate = roomRepository.count();

            assertEquals(initialRoomCount+1, roomCountAfterCreate);
        }

        @Test
        @DisplayName("Создание зала с невалидными данными")
        void createRoom_InValidData_ShouldReturnBadRequest() throws Exception {
            securitySetup.setUpSecurityContext("admin", List.of("ROLE_ADMIN"));
            String token = securitySetup.createTestToken("admin");

            long initialRoomCount = roomRepository.count();

            Integer roomCapacity = -50;
            Integer addressNumber = -5;
            List<String> trainerIds = UserTestDataFactory.createDefaultTestUsers().stream()
                    .filter(u -> u.getRoles().contains("ROLE_TRAINER"))
                    .map(User::getId)
                    .toList();
            String roomName = "New Training Room";

            CreateRoomDto createRoomDto = CreateRoomDto.builder()
                    .capacity(roomCapacity)
                    .location(new LocationPojo("", addressNumber))
                    .trainers(trainerIds)
                    .name(roomName)
                    .build();

            mockMvc.perform(post("/rooms")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(createRoomDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    JSONAssert.assertEquals(
                        "{" +
                        "  'errors': {" +
                        "    'closingTime': 'Часы закрытия не могут быть пустыми'," +
                        "    'workingDays': 'Рабочие дни не могут быть пустыми'," +
                        "    'openingTime': 'Часы открытия не могут быть пустыми'," +
                        "    'location.number': 'Номер комнаты должен быть положительным'," +
                        "    'location.address': 'Адрес не может быть пустым'," +
                        "    'sections': 'Секции не могут быть пустыми'," +
                        "    'capacity': 'Вместительность должна быть положительной'" +
                        "  }" +
                        "}",
                        responseBody, 
                        JSONCompareMode.LENIENT
                    );}
                );

            long roomCountAfterCreate = roomRepository.count();

            assertEquals(initialRoomCount, roomCountAfterCreate);
        }

        @Test
        @DisplayName("Создание зала с невалидными данными")
        void createRoom_ValidDataAndUnauthorizeUser_ShouldReturnForbidden() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            long initialRoomCount = roomRepository.count();

            Integer roomCapacity = -50;
            Integer addressNumber = -5;
            List<String> trainerIds = UserTestDataFactory.createDefaultTestUsers().stream()
                    .filter(u -> u.getRoles().contains("ROLE_TRAINER"))
                    .map(User::getId)
                    .toList();
            String roomName = "New Training Room";

            CreateRoomDto createRoomDto = CreateRoomDto.builder()
                    .capacity(roomCapacity)
                    .location(new LocationPojo("", addressNumber))
                    .trainers(trainerIds)
                    .name(roomName)
                    .build();

            mockMvc.perform(post("/rooms")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(createRoomDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
                            
            long roomCountAfterCreate = roomRepository.count();

            assertEquals(initialRoomCount, roomCountAfterCreate);
        }
    }

    @Nested
    @DisplayName("Обновление комнат")
    public class UpdateRoomTest {
    
        @Test
        @DisplayName("Обновление существующей комнаты")
        void updateRoom_ExistingRoomAndValidData_ShouldReturnsUpdatedRoom() throws JsonProcessingException, Exception {
            securitySetup.setUpSecurityContext("admin", List.of("ROLE_ADMIN"));
            String token = securitySetup.createTestToken("admin");

            long initialRoomCount = roomRepository.count();
            String existingId = TrainingTestDataFactory.roomId1;
            Room roomBeforeUpdate = roomRepository.findById(existingId).get();

            Integer roomCapacity = 50;
            List<String> trainerIds = userRepository.findAllByRoles("ROLE_TRAINER").stream()
                    .filter(u -> u.getRoles().contains("ROLE_TRAINER"))
                    .map(User::getId)
                    .toList();
            String roomName = "New Training Room";
            LocalTime openingTime = LocalTime.of(9, 0, 0);
            String workingDays = "ПН, СР, ПТ";

            UpdateRoomDto updateRoomDto = UpdateRoomDto.builder()
                    .capacity(roomCapacity)
                    .location(new LocaltionUpdatePojo(null, null))
                    .openingTime(openingTime)
                    .trainers(trainerIds)
                    .workingDays(workingDays)
                    .name(roomName)
                    .build();

            mockMvc.perform(put("/rooms/{roomId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateRoomDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.name").value(roomName),
                            jsonPath("$.capacity").value(roomCapacity),
                            jsonPath("$.location.address").value(roomBeforeUpdate.getLocation().getAddress()),
                            jsonPath("$.location.number").value(roomBeforeUpdate.getLocation().getNumber()),
                            jsonPath("$.openingTime").value(openingTime.format(DateTimeFormatter.ISO_LOCAL_TIME)),
                            jsonPath("$.closingTime").value(roomBeforeUpdate.getClosingTime().format(DateTimeFormatter.ISO_LOCAL_TIME)),
                            jsonPath("$.workingDays").value(workingDays),
                            jsonPath("$.sections", equalTo(roomBeforeUpdate.getSections())),
                            jsonPath("$.trainers", equalTo(trainerIds))
                    );
                            
            Room roomAfterUpdate = roomRepository.findById(existingId).get();
            long roomCountAfterUpdate = roomRepository.count();

            List<Training> trainings = trainingRepository.findAllByRoomId(existingId);
            trainings.forEach(training -> {
                assertNotNull(training.getRoom(), "Зал тренировки не должен быть null");
                assertEquals(roomBeforeUpdate.getId(), training.getRoom().getId(), 
                    "ID зала должен соответствовать обновленному залу");
                
                assertEquals(roomAfterUpdate.getName(), training.getRoom().getName(), 
                    "Название зала должно соответствовать обновленному залу");

                assertEquals(roomAfterUpdate.getCapacity(), training.getRoom().getCapacity(), 
                    "Название зала должно соответствовать обновленному залу");
            });

            assertEquals(initialRoomCount, roomCountAfterUpdate);
            assertEquals(roomBeforeUpdate.getSections(), roomAfterUpdate.getSections());
            assertEquals(roomBeforeUpdate.getLocation().getAddress(), roomAfterUpdate.getLocation().getAddress());
            assertEquals(roomBeforeUpdate.getLocation().getNumber(), roomAfterUpdate.getLocation().getNumber());
            assertEquals(roomBeforeUpdate.getClosingTime(), roomAfterUpdate.getClosingTime());
        }

        @Test
        @DisplayName("Обновление существующей комнаты невалидными данными")
        void updateRoom_ExistingRoomAndInValidData_ShouldReturnsBadRequest() throws JsonProcessingException, Exception {
            securitySetup.setUpSecurityContext("admin", List.of("ROLE_ADMIN"));
            String token = securitySetup.createTestToken("admin");

            long initialRoomCount = roomRepository.count();
            String existingId = TrainingTestDataFactory.roomId1;
            Room roomBeforeUpdate = roomRepository.findById(existingId).get();

            Integer roomCapacity = -50;

            UpdateRoomDto updateRoomDto = UpdateRoomDto.builder()
                    .capacity(roomCapacity)
                    .location(new LocaltionUpdatePojo(null, -5))
                    .build();

            mockMvc.perform(put("/rooms/{roomId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateRoomDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
                            
            Room roomAfterUpdate = roomRepository.findById(existingId).get();
            long roomCountAfterUpdate = roomRepository.count();
            List<Training> trainings = trainingRepository.findAllByRoomId(existingId);
            trainings.forEach(training -> {
                assertNotNull(training.getRoom(), "Зал тренировки не должен быть null");
                assertEquals(roomBeforeUpdate.getId(), training.getRoom().getId(), 
                    "ID зала должен соответствовать залу до обновления");
                
                assertEquals(roomBeforeUpdate.getName(), training.getRoom().getName(), 
                    "Название зала должно соответствовать залу до обновления");

                assertEquals(roomBeforeUpdate.getCapacity(), training.getRoom().getCapacity(), 
                    "Название зала должно соответствовать залу до обновления");
            });

            assertEquals(initialRoomCount, roomCountAfterUpdate);
            assertEquals(roomBeforeUpdate.getCapacity(), roomAfterUpdate.getCapacity());
            assertEquals(
                    roomBeforeUpdate.getTrainers().stream().map(t -> t.getId()).toList(), 
                    roomAfterUpdate.getTrainers().stream().map(t -> t.getId()).toList()
            );
            assertEquals(roomBeforeUpdate.getSections(), roomAfterUpdate.getSections());
            assertEquals(roomBeforeUpdate.getLocation().getAddress(), roomAfterUpdate.getLocation().getAddress());
            assertEquals(roomBeforeUpdate.getLocation().getNumber(), roomAfterUpdate.getLocation().getNumber());
            assertEquals(roomBeforeUpdate.getClosingTime(), roomAfterUpdate.getClosingTime());
        }

        @Test
        @DisplayName("Обновление не существующей комнаты")
        void updateRoom_NotExistingRoomAndValidData_ShouldReturnsNotFound() throws JsonProcessingException, Exception {
            securitySetup.setUpSecurityContext("admin", List.of("ROLE_ADMIN"));
            String token = securitySetup.createTestToken("admin");

            long initialRoomCount = roomRepository.count();
            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            UpdateRoomDto updateRoomDto = UpdateRoomDto.builder()
                    .build();

            mockMvc.perform(put("/rooms/{roomId}", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateRoomDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
                            
            long roomCountAfterUpdate = roomRepository.count();

            assertEquals(initialRoomCount, roomCountAfterUpdate);
        }

        @Test
        @DisplayName("Обновление cуществующей комнаты с нарушением прав доступа")
        void updateRoom_ExistingRoomAndValidDataAndUnauthorizeUser_ShouldReturnsForbidden() throws JsonProcessingException, Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            long initialRoomCount = roomRepository.count();
            String existingId = TrainingTestDataFactory.roomId1;
            Room roomBeforeUpdate = roomRepository.findById(existingId).get();

            Integer roomCapacity = 50;
            List<String> trainerIds = userRepository.findAllByRoles("ROLE_TRAINER").stream()
                    .filter(u -> u.getRoles().contains("ROLE_TRAINER"))
                    .map(User::getId)
                    .toList();
            String roomName = "New Training Room";
            LocalTime openingTime = LocalTime.of(9, 0, 0);
            String workingDays = "ПН, СР, ПТ";

            UpdateRoomDto updateRoomDto = UpdateRoomDto.builder()
                    .capacity(roomCapacity)
                    .location(new LocaltionUpdatePojo(null, null))
                    .openingTime(openingTime)
                    .trainers(trainerIds)
                    .workingDays(workingDays)
                    .name(roomName)
                    .build();

            mockMvc.perform(put("/rooms/{roomId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .content(objectMapper.writeValueAsString(updateRoomDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
                            
            Room roomAfterUpdate = roomRepository.findById(existingId).get();
            long roomCountAfterUpdate = roomRepository.count();

            assertEquals(initialRoomCount, roomCountAfterUpdate);
            assertEquals(roomBeforeUpdate.getSections(), roomAfterUpdate.getSections());
            assertEquals(roomBeforeUpdate.getLocation().getAddress(), roomAfterUpdate.getLocation().getAddress());
            assertEquals(roomBeforeUpdate.getLocation().getNumber(), roomAfterUpdate.getLocation().getNumber());
            assertEquals(roomBeforeUpdate.getClosingTime(), roomAfterUpdate.getClosingTime());
            
        }
    }

    @Nested
    @DisplayName("Получение залов")
    public class GetRoomsTest {

        @Test
        @DisplayName("Получение всех залов")
        void getRooms_ShouldReturnsRooms() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            long roomCount = roomRepository.count();
            mockMvc.perform(get("/rooms")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray(),
                            jsonPath("$.length()").value(roomCount)
                    );
        }

        @Test
        @DisplayName("Получение существующего зала по идентификатору")
        void getRoomsById_ExistingRoom_ShouldReturnsRoom() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            String existingId = TrainingTestDataFactory.roomId1;
            Room existingRoom = roomRepository.findById(existingId).get();

            mockMvc.perform(get("/rooms/{roomId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.id").value(existingRoom.getId()),
                            jsonPath("$.name").value(existingRoom.getName()),
                            jsonPath("$.capacity").value(existingRoom.getCapacity()),
                            jsonPath("$.location.address").value(existingRoom.getLocation().getAddress()),
                            jsonPath("$.location.number").value(existingRoom.getLocation().getNumber()),
                            jsonPath("$.openingTime").value(existingRoom.getOpeningTime().format(DateTimeFormatter.ISO_LOCAL_TIME)),
                            jsonPath("$.closingTime").value(existingRoom.getClosingTime().format(DateTimeFormatter.ISO_LOCAL_TIME)),
                            jsonPath("$.workingDays").value(existingRoom.getWorkingDays()),
                            jsonPath("$.sections", equalTo(existingRoom.getSections())),
                            jsonPath("$.trainers", equalTo(existingRoom.getTrainers().stream().map(t -> t.getId()).toList()))
                    );
        }

        @Test
        @DisplayName("Получение не существующего зала по идентификатору")
        void getRoomsById_NotExistingRoom_ShouldReturnsNotFound() throws Exception {
            securitySetup.setUpSecurityContext("client", List.of("ROLE_USER"));
            String token = securitySetup.createTestToken("client");

            String notExistingId = "653ef3a8zzzzzzz7bcdf3099";

            mockMvc.perform(get("/rooms/{roomId}", notExistingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(status().isNotFound());
        }

        @Test
        @DisplayName("Получение зала по идентификатору c неавторизованным пользователем")
        void getRoomsById_NotExistingRoom_ShouldReturnsUnauthorize() throws Exception {
            String existingId = TrainingTestDataFactory.roomId1;

            mockMvc.perform(get("/rooms/{roomId}", existingId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(status().isUnauthorized());
        }

        @Test
        @DisplayName("Получение залов c неавторизованным пользователем")
        void getRooms_UserUnauthorize_ShouldReturnsUnauthorize() throws Exception {
            mockMvc.perform(get("/rooms")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(status().isUnauthorized());
        }
    }
    
    @Nested
    @DisplayName("Удаление зала")
    public class DeleteRoomTest {
        @Test
        @DisplayName("Удаление зала по идентификатору")
        void deleteRoomById_ExistingRoom_ShouldReturnsOk() throws Exception {
            securitySetup.setUpSecurityContext("admin", List.of("ROLE_ADMIN"));
            String token = securitySetup.createTestToken("admin");

            String existingId = TrainingTestDataFactory.roomId1;

            mockMvc.perform(delete("/rooms/{roomId}", existingId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(status().isOk());

            List<Training> trainings = trainingRepository.findAllByRoomId(existingId);
            assertEquals(trainings.size(), 0);
        }
        
    }
}
