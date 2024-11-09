package com.example.gym.service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingClientDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingForStatistic;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.Section;
import com.example.gym.model.user.pojo.TrainerInfo;
import com.example.gym.repository.RoomRepository;
import com.example.gym.repository.TrainingRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;
import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.room.Room;
import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.trainer.TrainerPojo;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final ClientService clientService;
    private final Mapper modelMapper;
    private final RoomRepository roomRepository;

    public ResponseTrainingDto findTrainingById(String id) {
        Training training = getById(id);
        return modelMapper.toDto(training);
    }

    // public List<ResponseTrainingDto> findTrainingsByTrainerId(String trainerId) {
    //     List<Training> trainings = trainingRepository.findAllByTrainerId(trainerId);
    //     return trainings.stream()
    //             .map(s -> modelMapper.toDto(s))
    //             .toList();
    // }

    // public ResponseTrainingDto findTrainingByIdAndTrainerId(String trainingId, String trainerId) {
    //     // Optional<Training> optionalTraining = trainingRepository.findByIdAndTrainerId(trainingId, trainerId);
    //     // if (optionalTraining.isEmpty()) {
    //     //     throw new NoResultException("Тренировка с id %s не найдена".formatted(trainingId));
    //     // }

    //     Training training = optionalTraining.get();
    //     return modelMapper.toDto(training);
    // }

    @Transactional
    public ResponseTrainingDto createTraining(CreateTrainingDto dto, User trainer) {
        Training training = modelMapper.toModel(dto);
        TrainerPojo trainerPojo = modelMapper.toPojo(trainer);
        training.setTrainerPojo(trainerPojo);

        Optional<Room> optionalRoom = roomRepository.findById(dto.getRoomId());
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            RoomPojo roomPojo = modelMapper.toPojo(room);
            training.setRoom(roomPojo);
        }

        Training createdTraining = trainingRepository.save(training);
        return modelMapper.toDto(createdTraining);
    }

    public void deleteTraining(String trainingId) {
        trainingRepository.deleteById(trainingId);
    }

    @Transactional
    public void registrationClientForTraining(String trainigId, String clientId) {
        Training training = getById(trainigId);
        User client = clientService.getById(clientId);

        if (!training.isHasFreeRegistration()) {
            throw new IllegalArgumentException("Свободных записей нет");
        }

        List<ClientPojo> clientsInTraining = training.getClients();
        ClientPojo newClient = modelMapper.toClientPojo(client);
        clientsInTraining.add(newClient);
        training.setClients(clientsInTraining);

        Integer availableSlots = training.getAvailableSlots();
        if (availableSlots != 0) {
            Integer currentAvaiableSlots = availableSlots - 1;
            if (currentAvaiableSlots == 0) {
                training.setHasFreeRegistration(false);
            }

            training.setAvailableSlots(currentAvaiableSlots);
        }

        // TrainerPojo trainerPojo = training.getTrainerPojo();
        // Optional<User> optionalTrainer = userRepository.findById(trainerPojo.getId());
        // if (optionalTrainer.isPresent()) {
        //     User trainer = optionalTrainer.get();
        //     TrainerInfo trainerInfo = trainer.getTrainerInfo();
        //     trainerInfo.setFree(trainingRepository.findAllByTrainerId(trainer.getId()).stream()
        //         .anyMatch(trainig -> trainig.isHasFreeRegistration() == true));
            
        //     User savedTrainer = userRepository.save(trainer);
        //     TrainerPojo savedTrainerPojo = modelMapper.toPojo(savedTrainer);

        //     training.setTrainerPojo(savedTrainerPojo);
        // }

        trainingRepository.save(training);
    }

    @Transactional
    public ResponseTrainingDto updateTraining(String trainingId, CreateTrainingDto dto) {
        Training training = getById(trainingId);

        if (dto.getStartTime() != null && !training.getStartTime().equals(dto.getStartTime())) {
            training.setStartTime(dto.getStartTime());
        }

        if (dto.getEndTime() != null && !training.getEndTime().equals(dto.getEndTime())) {
            training.setEndTime(dto.getEndTime());
        }

        if (dto.getAvailableSlots() != null && !training.getAvailableSlots().equals(dto.getAvailableSlots())) {
            training.setAvailableSlots(dto.getAvailableSlots());
        }

        if (dto.getSection() != null && !training.getSection().getName().equals(dto.getSection())) {
            training.setSection(new Section(dto.getSection()));
        }

        if (dto.getRoomId() != null && !training.getRoom().getId().equals(dto.getRoomId())) {
            training.setRoom(modelMapper.toPojo(
                    roomRepository.findById(dto.getRoomId()).get()
            ));
        }
        
        Training updatedTraining = trainingRepository.save(training);
        return modelMapper.toDto(updatedTraining);
    }

    public List<ClientPojo> findTrainingClients(String trainingId) {
        Training training = getById(trainingId);
        return training.getClients();
    }

    public ResponseTrainingForStatistic findAllConductedTrainings() {
        ResponseTrainingForStatistic trainingsForStatistic = new ResponseTrainingForStatistic();
        List<ResponseTrainingDto> trainings = findAll().stream()
                .map(t -> modelMapper.toDto(t))
                .toList();
        trainingsForStatistic.setCount(trainings.size());
        trainingsForStatistic.setTrainings(trainings);
        return trainingsForStatistic;
    }

    public List<ResponseTrainingDto> findAllTrainigs() {
        return findAll().stream()
                .map(t -> modelMapper.toDto(t))
                .toList();
    }

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    public Training getById(String id) {
        Optional<Training> optionalTraining = trainingRepository.findById(id);
        if (optionalTraining.isEmpty()) {
            throw new NoResultException("Тренировка с id %s не найдена".formatted(id));
        }

        return optionalTraining.get();
    }
}
