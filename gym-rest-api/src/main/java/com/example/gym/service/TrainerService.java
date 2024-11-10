package com.example.gym.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gym.exception.InvalidDataException;
import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.trainer.UpdateTrainerDto;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.CreateTrainingDto;

import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.model.user.pojo.Section;
import com.example.gym.model.user.pojo.TrainerInfo;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final UniquenessCheckService uniquenessCheckService;
    private final UserRepository userRepository;
    private final TrainingService trainingService;
    private final Mapper modelMapper;


    public List<ResponseTrainerWithoutTrainingsDto> findAll(String section) {
        return userRepository.findAllByRoles(UserRoleType.ROLE_TRAINER.name()).stream()
            .filter(trainer -> section == null || section.isEmpty() || section.isBlank() ||
                    trainer.getTrainerInfo().getSectionNames().contains(section))
            .map(modelMapper::toDtoWithoutTraining)
            .toList();
    }

    // public List<ResponseTrainerWithoutTrainingsDto> findAllFree(String section) {
    //     return userRepository.findAllByFreeAndRoleIndex(true, ROLE_TRAINER_INDEX).stream()
    //             .filter(trainer -> section == null || section.isEmpty() || section.isBlank() ||
    //                     trainer.getTrainerInfo().getSectionNames().contains(section))
    //             .map(modelMapper::toDtoWithoutTraining)
    //             .toList();
    // }

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
    public ResponseTrainerDto updateTrainer(String id, UpdateTrainerDto dto) throws UniquenessViolationException, ResourceNotFoundException {
        User trainer = getById(id);

        if (dto.getName() != null && !trainer.getName().equals(dto.getName())) {
            trainer.setName(dto.getName());
        }

        if (dto.getSurname() != null && !trainer.getSurname().equals(dto.getSurname())) {
            trainer.setSurname(dto.getSurname());
        }

        if (dto.getEmail() != null && !trainer.getEmail().equals(dto.getEmail())) {
            if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
                throw new UniquenessViolationException("Пользователь с электронной почтой %s уже существует"
                        .formatted(dto.getEmail()));
            }

            trainer.setEmail(dto.getEmail());
        }

        if (dto.getPhoneNumber() != null && !trainer.getPhoneNumber().equals(dto.getPhoneNumber())) {
            if (uniquenessCheckService.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
                throw new UniquenessViolationException("Пользователь с номером телефона %s уже существует"
                        .formatted(dto.getPhoneNumber()));
            }

            trainer.setPhoneNumber(dto.getPhoneNumber());
        }

        TrainerInfo trainerInfo = trainer.getTrainerInfo();
        if (dto.getExperience() != null && trainerInfo.getExperience() != dto.getExperience()) {
            trainerInfo.setExperience(dto.getExperience());
        }

        if (dto.getSpecialization() != null && trainerInfo.getQualification() != dto.getSpecialization()) {
            trainerInfo.setQualification(dto.getSpecialization());
        }

        if (dto.getHourlyRate() != null && trainerInfo.getHourlyRate() != dto.getHourlyRate()) {
            trainerInfo.setHourlyRate(dto.getHourlyRate());
        }

        if (dto.getSections() != null) {
            trainerInfo.setSections(getSections(dto.getSections()));
        }

        trainer.setTrainerInfo(trainerInfo);

        User updatedTrainer = userRepository.save(trainer);
        return modelMapper.toTrainerDto(updatedTrainer);
    } 

    private List<Section> getSections(List<String> sectionsList) {
        Set<Section> sections = new HashSet<>();
        for (String sectionName : sectionsList) {
            sections.add(new Section(sectionName));
        }

        return sections.stream().toList();
    }

    public void deleteTrainer(String id) {
        userRepository.deleteById(id);
    }

    // @Transactional
    // public ResponseTrainerDto setTrainerHourlyRate(String id, HourlyRateDto dto) {
    //     User trainer = getById(id);
    //     trainer.setHourlyRate(dto.getHourlyRate());
    //     User updatedTrainer = userRepository.save(trainer);
    //     return modelMapper.toTrainerDto(updatedTrainer);
    // }

    public ResponseTrainerDto findById(String id) throws ResourceNotFoundException {
        ResponseTrainerDto trainer = modelMapper.toTrainerDto(getById(id));
        return trainer;
    }

    // public List<ResponseTrainingDto> findTrainingsByTrainerId(String id) {
    //     getById(id);
    //     return trainingService.findTrainingsByTrainerId(id);
    // }

    @Transactional
    public ResponseTrainingDto createTraining(CreateTrainingDto dto, String trainerId) throws ResourceNotFoundException, InvalidDataException {
        User trainer = getById(trainerId);

        LocalDateTime newStartTime = dto.getStartTime();
        LocalDateTime newEndTime = dto.getEndTime();

        if (newStartTime.isAfter(newEndTime)) {
            throw new InvalidDataException("Время новой тренировки пересекается с существующей тренировкой.");
        }
    
        List<Training> overlappingTrainings = trainingService.findAll().stream()
                .filter(training -> training.getTrainerPojo().getId().equals(trainerId))
                .filter(training -> 
                    (newStartTime.isBefore(training.getEndTime()) && newEndTime.isAfter(training.getStartTime()))
                )
                .collect(Collectors.toList());
        
        if (!overlappingTrainings.isEmpty()) {
            throw new InvalidDataException("Время новой тренировки пересекается с существующей тренировкой.");
        }
        return trainingService.createTraining(dto, trainer);
    }

    // public List<ResponseTrainerDto> findTrainersBySection(Section section) {
    //     List<User> trainers = userRepository.findAllBySections(section);
    //     return trainers.stream()
    //             .map(t -> modelMapper.toTrainerDto(t))
    //             .toList();
    // }

    public List<ResponseTrainingDto> findAllTrainigs() {
        return trainingService.findAllTrainigs();
    }

    // public List<ResponseTrainerForStatistic> getTrainersActivity() {
    //     List<User> trainers = userRepository.findAllByRoleIndex(ROLE_TRAINER_INDEX);
    //     List<Training> trainings = trainingService.findAll();
    //     return trainers.stream()    
    //             .map(t -> {
    //                 ResponseTrainerForStatistic responseTrainerForStatistic = new ResponseTrainerForStatistic();
    //                 responseTrainerForStatistic.setTrainer(modelMapper.toTrainerDto(t));
    //                 List<Training> filterByTrainer = trainings.stream()
    //                         .filter(training -> training.getTrainer() == t)
    //                         .toList();
    //                 responseTrainerForStatistic.setCount(filterByTrainer.size());
    //                 responseTrainerForStatistic.setTrainings(filterByTrainer.stream()
    //                         .map(training -> modelMapper.toDto(training))
    //                         .toList());  
    //                 responseTrainerForStatistic.setProfit(filterByTrainer.stream()
    //                         .map(training -> training.getDuration())
    //                         .reduce(0f, (Float x, Float y) -> x + y) * t.getHourlyRate());  
    //                 return responseTrainerForStatistic;
    //             })
    //             .toList();
    // }

    // public List<ResponseClientDto> findTrainingClients(String trainerId, String trainingId) {
    //     getById(trainerId);
    //     return trainingService.findTrainingClients(trainingId);
    // }

    public User getById(String id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тренер с id %s не найден".formatted(id)));

    }

}
