package com.example.gym.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.Section;
import com.example.gym.model.user.pojo.TrainerInfo;
import com.example.gym.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabasePreloader implements ApplicationRunner {

    private final UserRepository userRepository;
    
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createAdmin();
        createClient();
        createTrainer();
    }

    private void createAdmin() {
        userRepository.deleteAll();
        if (userRepository.findAllByRoleIndex(UserRoleType.ROLE_ADMIN.ordinal()).size() == 0) {
            User admin = new User(
                "gym-admin", 
                "gym-admin", 
                "gym-admin", 
                adminPassword, 
                "",
                List.of(UserRoleType.ROLE_ADMIN.name()),
                UserRoleType.ROLE_ADMIN.ordinal());

            userRepository.save(admin);
        }
    }

    private void createClient() {
        if (userRepository.findAllByRoleIndex(UserRoleType.ROLE_USER.ordinal()).size() == 0) {
            User client = new User(
                "client", 
                "client", 
                "client@mail.ru", 
                "client", 
                "+79998887766",
                List.of(UserRoleType.ROLE_USER.name()),
                UserRoleType.ROLE_USER.ordinal());


            ClientInfo clientInfo = new ClientInfo(100, new ArrayList<>(), new ArrayList<>());
            client.setClientInfo(clientInfo);
            userRepository.save(client);
        }
    }

    private void createTrainer() {
        if (userRepository.findAllByRoleIndex(UserRoleType.ROLE_TRAINER.ordinal()).size() == 0) {
            User trainer = new User(
                "trainer", 
                "gym", 
                "trainer@mail.ru", 
                "trainer", 
                "+78887776655",
                List.of(UserRoleType.ROLE_TRAINER.name()),
                UserRoleType.ROLE_TRAINER.ordinal());

            TrainerInfo trainerInfo = new TrainerInfo(
                    1, 
                    2000.0, 
                    "Пловец", 
                    true,
                    List.of(new Section("Плавние")));
                
            trainer.setTrainerInfo(trainerInfo);

            userRepository.save(trainer);
        }
    }

}
