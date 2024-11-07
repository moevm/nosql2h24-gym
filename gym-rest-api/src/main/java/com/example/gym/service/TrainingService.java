package com.example.gym.service;

import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingClientDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingForStatistic;
import com.example.gym.model.user.User;
import com.example.gym.repository.TrainingRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final ClientService clientService;
    private final Mapper modelMapper;

    public ResponseTrainingClientDto findTrainingById(String id) {
        Training training = getById(id);
        ResponseTrainingDto trainingDto = modelMapper.toDto(training);
        List<ResponseClientDto> clientDto = training.getClients().stream()
                .map(modelMapper::toClientDto)
                .toList();
        return new ResponseTrainingClientDto(trainingDto, clientDto);
    }

    public List<ResponseTrainingDto> findTrainingsByTrainerId(String trainerId) {
        List<Training> trainings = trainingRepository.findAllByTrainerId(trainerId);
        return trainings.stream()
                .map(s -> modelMapper.toDto(s))
                .toList();
    }

    public ResponseTrainingDto findTrainingByIdAndTrainerId(String trainingId, String trainerId) {
        Optional<Training> optionalTraining = trainingRepository.findByIdAndTrainerId(trainingId, trainerId);
        if (optionalTraining.isEmpty()) {
            throw new NoResultException("Тренировка с id %s не найдена".formatted(trainingId));
        }

        Training training = optionalTraining.get();
        return modelMapper.toDto(training);
    }

    @Transactional
    public ResponseTrainingDto createTraining(CreateTrainingDto dto, User trainer) {
        Training training = modelMapper.toModel(dto);
        training.setTrainer(trainer);
        Duration duration = Duration.between(training.getStartTime(), training.getEndTime());
        training.setDuration((float) duration.toMinutes() / 60);
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

        Set<User> clientsInTraining = training.getClients();
        clientsInTraining.add(client);
        training.setClients(clientsInTraining);
        training.setHasFreeRegistration(false);

        User trainer = training.getTrainer();
        trainer.setFree(trainingRepository.findAllByTrainerId(trainer.getId()).stream()
                .anyMatch(trainig -> trainig.isHasFreeRegistration() == true));

        trainingRepository.save(training);
        userRepository.save(trainer);
    }

    @Transactional
    public ResponseTrainingDto updateTraining(String trainingId, CreateTrainingDto dto) {
        Training training = getById(trainingId);
        training.setStartTime(dto.getStartTime());
        
        Training updatedTraining = trainingRepository.save(training);
        return modelMapper.toDto(updatedTraining);
    }

    public List<ResponseClientDto> findTrainingClients(String trainingId) {
        Training training = getById(trainingId);
        return training.getClients().stream()
                .map(c -> modelMapper.toClientDto(c))
                .toList();
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
