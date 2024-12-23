package com.example.gym.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.trainer.TrainerPojo;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.model.user.pojo.TrainerInfo;

public class UserTestDataFactory {

    public static final String CLIENT_ID = new ObjectId().toHexString();
    public static final String TRAINER_ID = new ObjectId().toHexString();
    public static final String CLIENT_ID_FOR_REGISTRAION = new ObjectId().toHexString();

    public static List<User> createDefaultTestUsers() {
        return List.of(
                User.builder()
                    .id(CLIENT_ID)
                    .name("Test")
                    .surname("User")
                    .password("test1")
                    .email("client")
                    .phoneNumber("+77777777777")
                    .roles(List.of("ROLE_USER"))
                    .clientInfo(new ClientInfo(3000, new ArrayList<>(), new ArrayList<>()))
                    .createdAt(LocalDateTime.now())
                    .gender(GenderType.FEMALE)
                    .build(),
                User.builder()
                    .id(CLIENT_ID_FOR_REGISTRAION)
                    .name("Иван")
                    .gender(GenderType.MALE)
                    .surname("Иванов")
                    .password("test1")
                    .email("client_for_registration")
                    .phoneNumber("+132134145411")
                    .clientInfo(new ClientInfo(100, new ArrayList<>(), new ArrayList<>()))
                    .birthday(LocalDateTime.of(2024, 12, 1, 00, 00, 00))
                    .roles(List.of("ROLE_USER"))
                    .createdAt(LocalDateTime.of(2024, 12, 2, 12, 30, 00))
                    .build(),


                User.builder()
                    .id(TRAINER_ID)
                    .name("Иван")
                    .surname("Иванов")
                    .password("test2")
                    .email("trainer")
                    .phoneNumber("+66666666666")
                    .roles(List.of("ROLE_TRAINER"))
                    .gender(GenderType.MALE)
                    .birthday(LocalDateTime.of(2024, 11, 30, 00, 00, 00))
                    .trainerInfo(new TrainerInfo(2, 3000d, "Мастер спорта", true, List.of("Бокс")))
                    .build(),
                User.builder()
                    .name("Павел")
                    .surname("Иванченко")
                    .password("test2")
                    .email("trainer3")
                    .phoneNumber("+66661694666")
                    .roles(List.of("ROLE_TRAINER"))
                    .gender(GenderType.MALE)
                    .birthday(LocalDateTime.of(2024, 12, 1, 00, 00, 00))
                    .trainerInfo(new TrainerInfo(2, 1000d, "Мастер спорта", true, List.of("Бокс", "Бег")))
                    .build(),
                User.builder()
                    .name("Trainer2")
                    .surname("test")
                    .password("test2")
                    .email("trainer2")
                    .phoneNumber("+66666462666")
                    .roles(List.of("ROLE_TRAINER"))
                    .gender(GenderType.FEMALE)
                    .trainerInfo(new TrainerInfo(1, 1500d, "Мастер", true, List.of("Плавание")))
                    .build(),


                User.builder()
                    .name("Test")
                    .surname("Admin")
                    .password("test3")
                    .email("admin")
                    .phoneNumber("+55555555555")
                    .roles(List.of("ROLE_ADMIN"))
                    .build()
        );
    }

    public static ClientPojo getDefaultClientPojo() {
        return new ClientPojo(CLIENT_ID, "Test", "User", GenderType.MALE, 0, LocalDateTime.now());
    }

    public static TrainerPojo getDefaultTrainerPojo() {
        return new TrainerPojo(TRAINER_ID, "Иван", "Иванов", GenderType.MALE, "Мастер спорта", 3000d);
    }
    
}
