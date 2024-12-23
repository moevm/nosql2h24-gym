package com.example.gym.config;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.room.LocationPojo;
import com.example.gym.model.room.Room;
import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.trainer.TrainerPojo;
import com.example.gym.model.training.Training;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.model.user.pojo.Section;

public class TrainingTestDataFactory {

    public static final String roomId1 = new ObjectId().toHexString();
    public static final String roomId2 = new ObjectId().toHexString();

    public static final String DEFAULT_TRAINING_ID = new ObjectId().toHexString();
    public static final String DEFAULT_TRAINING_ID_WITH_CLIENTS = new ObjectId().toHexString();
    public static final String DEFAULT_TRAINING_ID_WITH_NOT_AVAILABLE_SLOTS = new ObjectId().toHexString();

    public static List<Room> getDefaultRooms() {
        return List.of(
            Room.builder()
                .id(roomId1)
                .name("House Holl")
                .capacity(50)
                .closingTime(LocalTime.now().plusHours(3))
                .openingTime(LocalTime.now().minusHours(3))
                .location(new LocationPojo("Address", 5))
                .sections(List.of("Section"))
                .workingDays("ПН, ЧТ")
                .trainers(List.of(new TrainerPojo("testId", "Trainer", "Test", GenderType.MALE, "Qualification", 300d)))
                .build(),
            Room.builder()
                .id(roomId2)
                .name("House")
                .capacity(100)
                .closingTime(LocalTime.now().plusHours(3))
                .openingTime(LocalTime.now().minusHours(3))
                .location(new LocationPojo("Training Room", 10))
                .sections(List.of("Section"))
                .workingDays("ВТ, ЧТ")
                .trainers(List.of(new TrainerPojo("testId", "Trainer", "Test", GenderType.MALE, "Qualification", 300d)))
                .build()
        );
    }

    public static List<Training> getDefaultTraining() {

            RoomPojo roomPojo1 = new RoomPojo(roomId1, "House Holl", 50);
            RoomPojo roomPojo2 = new RoomPojo(roomId2, "House", 15);

            return List.of(
                Training.builder()
                    .id(DEFAULT_TRAINING_ID)
                    .availableSlots(10)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Алексей", "Иванов", GenderType.MALE, "Мастер спорта", 300d))
                    .startTime(LocalDateTime.of(2024, 12, 19, 15, 45, 0))
                    .endTime(LocalDateTime.of(2024, 12, 19, 17, 45, 0))
                    .section(new Section("Групповая силовая"))
                    .room(roomPojo1)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(10)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Иван", "Петров", GenderType.MALE, "Тренер по кардио", 250d))
                    .startTime(LocalDateTime.of(2024, 12, 18, 22, 45, 0))
                    .endTime(LocalDateTime.of(2024, 12, 18, 23, 45, 0))
                    .section(new Section("Кардио"))
                    .room(roomPojo1)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(8)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Мария", "Смирнова", GenderType.FEMALE, "Инструктор йоги", 200d))
                    .startTime(LocalDateTime.of(2024, 12, 2, 18, 0, 0))
                    .endTime(LocalDateTime.of(2024, 12, 2, 19, 30, 0))
                    .section(new Section("Йога"))
                    .room(roomPojo1)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(12)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Дмитрий", "Козлов", GenderType.MALE, "Тренер по боксу", 280d))
                    .startTime(LocalDateTime.of(2024, 12, 5, 19, 30, 0))
                    .endTime(LocalDateTime.of(2024, 12, 5, 21, 0, 0))
                    .section(new Section("Бокс"))
                    .room(roomPojo1)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(6)
                    .hasFreeRegistration(false)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Елена", "Попова", GenderType.FEMALE, "Инструктор пилатеса", 220d))
                    .startTime(LocalDateTime.of(2024, 12, 10, 17, 15, 0))
                    .endTime(LocalDateTime.of(2024, 12, 10, 18, 45, 0))
                    .section(new Section("Групповая силовая"))
                    .room(roomPojo1)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(15)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Сергей", "Федоров", GenderType.MALE, "Тренер по функциональному тренингу", 290d))
                    .startTime(LocalDateTime.of(2024, 12, 7, 20, 0, 0))
                    .endTime(LocalDateTime.of(2024, 12, 7, 21, 30, 0))
                    .section(new Section("Функциональный тренинг"))
                    .room(roomPojo2)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(10)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                    .startTime(LocalDateTime.of(2024, 12, 16, 19, 0, 0))
                    .endTime(LocalDateTime.of(2024, 12, 16, 20, 0, 0))
                    .section(new Section("Зумба"))
                    .room(roomPojo2)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(7)
                    .hasFreeRegistration(false)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Максим", "Николаев", GenderType.MALE, "Тренер по кроссфиту", 320d))
                    .startTime(LocalDateTime.of(2024, 12, 20, 18, 30, 0))
                    .endTime(LocalDateTime.of(2024, 12, 20, 20, 0, 0))
                    .section(new Section("Кроссфит"))
                    .room(roomPojo2)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(9)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Наталья", "Федорова", GenderType.FEMALE, "Инструктор стретчинга", 190d))
                    .startTime(LocalDateTime.of(2024, 12, 8, 17, 45, 0))
                    .endTime(LocalDateTime.of(2024, 12, 8, 19, 0, 0))
                    .section(new Section("Стретчинг"))
                    .room(roomPojo2)
                    .build(),
                
                Training.builder()
                    .id(new ObjectId().toHexString())
                    .availableSlots(11)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Максим", "Соколов", GenderType.MALE, "Тренер по танцам", 240d))
                    .startTime(LocalDateTime.of(2024, 12, 18, 20, 15, 0))
                    .endTime(LocalDateTime.of(2024, 12, 18, 21, 45, 0))
                    .section(new Section("Танцы"))
                    .room(roomPojo2)
                    .build(),

                Training.builder()
                    .id(DEFAULT_TRAINING_ID_WITH_CLIENTS)
                    .availableSlots(11)
                    .hasFreeRegistration(true)
                    .clients(List.of(UserTestDataFactory.getDefaultClientPojo()))
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Ян", "Соколов", GenderType.MALE, "Тренер по танцам", 240d))
                    .startTime(LocalDateTime.of(2024, 12, 2, 20, 15, 0))
                    .endTime(LocalDateTime.of(2024, 12, 2, 21, 45, 0))
                    .section(new Section("Танцы"))
                    .room(roomPojo2)
                    .build(),

                Training.builder()
                    .id(DEFAULT_TRAINING_ID_WITH_NOT_AVAILABLE_SLOTS)
                    .availableSlots(1)
                    .hasFreeRegistration(true)
                    .clients(new ArrayList<>())
                    .trainer(new TrainerPojo(new ObjectId().toHexString(), "Ян", "Соколов", GenderType.MALE, "Тренер по танцам", 240d))
                    .startTime(LocalDateTime.of(2024, 11, 2, 20, 15, 0))
                    .endTime(LocalDateTime.of(2024, 11, 2, 21, 45, 0))
                    .section(new Section("Танцы"))
                    .room(roomPojo2)
                    .build()
            );
    }

    public static List<Training> getTrainingsForProfitStatistics() {

        RoomPojo roomPojo = new RoomPojo(roomId1, "House Holl", 50);

        return List.of(
            Training.builder()
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.of(2024, 8, 3, 20, 15, 0))
                .endTime(LocalDateTime.of(2024, 8, 3, 21, 45, 0))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build(),
            Training.builder()
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.of(2024, 11, 2, 20, 15, 0))
                .endTime(LocalDateTime.of(2024, 11, 2, 21, 45, 0))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build(),
            Training.builder()
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build(),
            Training.builder()
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo(), new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build(),
            Training.builder()
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build()
        );
    }

    public static final String trainingId1 = new ObjectId().toHexString();
    public static final String trainingId2 = new ObjectId().toHexString();
    public static final String trainingId3 = new ObjectId().toHexString();
    public static final String trainingId4 = new ObjectId().toHexString();
    public static final String trainingId5 = new ObjectId().toHexString();

    public static List<Training> getTrainingsForTrainingsStatistics() {

        RoomPojo roomPojo = new RoomPojo(roomId1, "House Holl", 50);

        return List.of(
            Training.builder()
                .id(trainingId1)
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.of(2024, 8, 3, 20, 15, 0))
                .endTime(LocalDateTime.of(2024, 8, 3, 21, 45, 0))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build(),
            Training.builder()
                .id(trainingId2)
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.of(2024, 11, 2, 20, 15, 0))
                .endTime(LocalDateTime.of(2024, 11, 2, 22, 45, 0))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build(),
            Training.builder()
                .id(trainingId3)
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.of(2024, 11, 3, 20, 15, 0))
                .endTime(LocalDateTime.of(2024, 11, 3, 21, 45, 0))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build(),
            Training.builder()
                .id(trainingId4)
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo(), new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.of(2024, 11, 13, 20, 15, 0))
                .endTime(LocalDateTime.of(2024, 11, 13, 21, 45, 0))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build(),
            Training.builder()
                .id(trainingId5)
                .availableSlots(1)
                .hasFreeRegistration(true)
                .clients(List.of(new ClientPojo(), new ClientPojo(), new ClientPojo()))
                .trainer(UserTestDataFactory.getDefaultTrainerPojo())
                .startTime(LocalDateTime.of(2024, 11, 14, 20, 15, 0))
                .endTime(LocalDateTime.of(2024, 11, 14, 21, 45, 0))
                .section(new Section("Танцы"))
                .room(roomPojo)
                .build()
        );
    }
}
