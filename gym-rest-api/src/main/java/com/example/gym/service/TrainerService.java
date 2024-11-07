package com.example.gym.service;

import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.section.Section;
import com.example.gym.model.trainer.HourlyRateDto;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.trainer.ResponseTrainerForStatistic;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.trainer.UpdateTrainerDto;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingClientDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.repository.SectionRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final UniquenessCheckService uniquenessCheckService;
    private final UserRepository userRepository;
    private final TrainingService trainingService;
    private final SectionRepository sectionRepository;
    private final Mapper modelMapper;

    private final static Integer ROLE_TRAINER_INDEX = UserRoleType.ROLE_TRAINER.ordinal();

    public List<ResponseTrainerWithoutTrainingsDto> findAll(String section) {
        return userRepository.findAllByRoleIndex(ROLE_TRAINER_INDEX).stream()
            .filter(trainer -> section == null || section.isEmpty() || section.isBlank() ||
                    trainer.getSectionNames().contains(section))
            .map(modelMapper::toDtoWithoutTraining)
            .toList();
    }

    public List<ResponseTrainerWithoutTrainingsDto> findAllFree(String section) {
        return userRepository.findAllByFreeAndRoleIndex(true, ROLE_TRAINER_INDEX).stream()
                .filter(trainer -> section == null || section.isEmpty() || section.isBlank() ||
                        trainer.getSectionNames().contains(section))
                .map(modelMapper::toDtoWithoutTraining)
                .toList();
    }

    // @Transactional
    // public ResponseTrainerDto createTrainer(CreateTrainerDto dto) {
    //     if (!findByEmail(dto.getEmail()).isEmpty()) {
    //         throw new IllegalArgumentException("Пользователь с такой электронной почтой уже существует");
    //     }

    //     if (!findByPhoneNumber(dto.getPhoneNumber()).isEmpty()) {
    //         throw new IllegalArgumentException("Пользователь с таким номером телефона уже существует");
    //     }
    //     Trainer trainer = modelMapper.toModel(dto);
    //     Trainer createdTrainer = trainerRepository.save(trainer);
    //     return modelMapper.toDto(createdTrainer);
    // }

    @Transactional
    public ResponseTrainerDto updateTrainer(String id, UpdateTrainerDto dto) {
        User trainer = getById(id);

        if (!trainer.getName().equals(dto.getName()) && dto.getName() != null) {
            trainer.setName(dto.getName());
        }

        if (!trainer.getSurname().equals(dto.getSurname()) && dto.getSurname() != null) {
            trainer.setSurname(dto.getSurname());
        }

        if (!trainer.getEmail().equals(dto.getEmail()) && dto.getEmail() != null) {
            if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с такой электронной почтой уже существует");
            }

            trainer.setEmail(dto.getEmail());
        }

        if (!trainer.getPhoneNumber().equals(dto.getPhoneNumber()) && dto.getPhoneNumber() != null) {
            if (uniquenessCheckService.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с таким номером телефона уже существует");
            }

            trainer.setPhoneNumber(dto.getPhoneNumber());
        }

        if (trainer.getExperience() != dto.getExperience() && dto.getExperience() != null) {
            trainer.setExperience(dto.getExperience());
        }

        if (trainer.getQualification() != dto.getSpecialization() && dto.getSpecialization() != null) {
            trainer.setQualification(dto.getSpecialization());
        }

        if (trainer.getHourlyRate() != dto.getHourlyRate() && dto.getHourlyRate() != null) {
            trainer.setHourlyRate(dto.getHourlyRate());
        }

        if (dto.getSections() != null) {
            trainer.setSections(getSections(dto.getSections()));
        }

        User updatedTrainer = userRepository.save(trainer);
        return modelMapper.toTrainerDto(updatedTrainer);
    } 

    private Set<Section> getSections(List<String> sectionsList) {
        Set<Section> sections = new HashSet<>();
        for (String sectionName : sectionsList) {
            Optional<Section> optionalSection = sectionRepository.findByName(sectionName);
            
            if (optionalSection.isPresent()) {
                sections.add(optionalSection.get());
            } else {
                throw new NoResultException("Секция с названием " + sectionName + " не найдена");
            }
        }

        return sections;
    }

    public void deleteTrainer(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public ResponseTrainerDto setTrainerHourlyRate(String id, HourlyRateDto dto) {
        User trainer = getById(id);
        trainer.setHourlyRate(dto.getHourlyRate());
        User updatedTrainer = userRepository.save(trainer);
        return modelMapper.toTrainerDto(updatedTrainer);
    }

    public ResponseTrainerDto findById(String id) {
        ResponseTrainerDto trainer = modelMapper.toTrainerDto(getById(id));
        List<ResponseTrainingDto> trainings = trainingService.findTrainingsByTrainerId(id);
        trainer.setTrainings(trainings);
        return trainer;
    }

    public List<ResponseTrainingDto> findTrainingsByTrainerId(String id) {
        getById(id);
        return trainingService.findTrainingsByTrainerId(id);
    }

    @Transactional
    public ResponseTrainingDto createTraining(CreateTrainingDto dto, String trainerId) {
        User trainer = getById(trainerId);

        LocalDateTime newStartTime = dto.getStartTime();
        LocalDateTime newEndTime = dto.getEndTime();

        if (newStartTime.isAfter(newEndTime)) {
            throw new IllegalArgumentException("Время начала тренировки должно быть раньше времени окончания.");
        }
    
        List<Training> overlappingTrainings = trainingService.findAll().stream()
                .filter(training -> training.getTrainer().getId().equals(trainerId))
                .filter(training -> 
                    (newStartTime.isBefore(training.getEndTime()) && newEndTime.isAfter(training.getStartTime()))
                )
                .collect(Collectors.toList());
        
        if (!overlappingTrainings.isEmpty()) {
            throw new IllegalArgumentException("Время новой тренировки пересекается с существующей тренировкой.");
        }
        return trainingService.createTraining(dto, trainer);
    }

    public void deleteTrainingByTrainer(String trainerId, String trainingId) {
        getById(trainingId);
        trainingService.deleteTraining(trainingId);
    }

    public void deleteTraining(String trainingId) {
        trainingService.deleteTraining(trainingId);
    }

    @Transactional
    public ResponseTrainingDto updateTraining(String trainerId, String trainingId, CreateTrainingDto dto) {
        getById(trainerId);
        return trainingService.updateTraining(trainingId, dto);
    }

    public ResponseTrainingClientDto findTrainingsByIdAndTrainerId(String trainerId, String trainingId) {
        ResponseTrainingDto training = trainingService.findTrainingByIdAndTrainerId(trainingId, trainerId);
        List<ResponseClientDto> clients = trainingService.findTrainingClients(trainingId);
        return new ResponseTrainingClientDto(training, clients);
    }

    public List<ResponseTrainerDto> findTrainersBySection(Section section) {
        List<User> trainers = userRepository.findAllBySections(section);
        return trainers.stream()
                .map(t -> modelMapper.toTrainerDto(t))
                .toList();
    }

    public List<ResponseTrainingDto> findAllTrainigs() {
        return trainingService.findAllTrainigs();
    }

    @Transactional
    public void registrationClientForTraining(String trainerId, String trainingId, String clientId) {
        User trainer = getById(trainerId);
        trainingService.registrationClientForTraining(trainingId, clientId);
        System.out.println(trainingService.findAll().stream()
                .filter(training -> training.getTrainer().getId().equals(trainerId))
                .anyMatch(training -> training.isHasFreeRegistration() == true));

        trainer.setFree(trainingService.findAll().stream()
                .filter(training -> training.getTrainer().getId().equals(trainerId))
                .anyMatch(training -> training.isHasFreeRegistration() == true));
        userRepository.save(trainer);
    }

    public List<ResponseTrainerForStatistic> getTrainersActivity() {
        List<User> trainers = userRepository.findAllByRoleIndex(ROLE_TRAINER_INDEX);
        List<Training> trainings = trainingService.findAll();
        return trainers.stream()    
                .map(t -> {
                    ResponseTrainerForStatistic responseTrainerForStatistic = new ResponseTrainerForStatistic();
                    responseTrainerForStatistic.setTrainer(modelMapper.toTrainerDto(t));
                    List<Training> filterByTrainer = trainings.stream()
                            .filter(training -> training.getTrainer() == t)
                            .toList();
                    responseTrainerForStatistic.setCount(filterByTrainer.size());
                    responseTrainerForStatistic.setTrainings(filterByTrainer.stream()
                            .map(training -> modelMapper.toDto(training))
                            .toList());  
                    responseTrainerForStatistic.setProfit(filterByTrainer.stream()
                            .map(training -> training.getDuration())
                            .reduce(0f, (Float x, Float y) -> x + y) * t.getHourlyRate());  
                    return responseTrainerForStatistic;
                })
                .toList();
    }

    public List<ResponseClientDto> findTrainingClients(String trainerId, String trainingId) {
        getById(trainerId);
        return trainingService.findTrainingClients(trainingId);
    }

    public User getById(String id) throws IllegalArgumentException {
        Optional<User> optionalTrainer = userRepository.findById(id);
        if (optionalTrainer.isEmpty()) {
            throw new NoResultException("Тренер с id %s не найден".formatted(id));
        }
        return optionalTrainer.get();
    }

}
