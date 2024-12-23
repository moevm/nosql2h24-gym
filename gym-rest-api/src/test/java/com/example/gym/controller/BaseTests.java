package com.example.gym.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.gym.config.SecuritySetup;
import com.example.gym.config.TestConfig;
import com.example.gym.config.TrainingTestDataFactory;
import com.example.gym.config.UserTestDataFactory;
import com.example.gym.model.room.Room;
import com.example.gym.model.training.Training;
import com.example.gym.model.user.User;
import com.example.gym.repository.RoomRepository;
import com.example.gym.repository.TrainingRepository;
import com.example.gym.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(
    classes = {
        TestConfig.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc(addFilters = true, printOnlyOnFailure = false)
@ActiveProfiles("test")
public class BaseTests {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TrainingRepository trainingRepository;

    @Autowired
    protected RoomRepository roomRepository;

    @Autowired 
    protected SecuritySetup securitySetup;

    @BeforeEach
    @Transactional
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .alwaysDo(print())
            .build();

        trainingRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();

        List<User> users = UserTestDataFactory.createDefaultTestUsers();
        List<Room> rooms = TrainingTestDataFactory.getDefaultRooms();
        List<Training> trainings = TrainingTestDataFactory.getDefaultTraining();

        userRepository.saveAll(users);
        trainingRepository.saveAll(trainings);
        roomRepository.saveAll(rooms);
    }

}
