package com.example.gym.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.gym.exception.InvalidDataException;
import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.dto.statistics.trainer.ResponseTrainingForStatistics;
import com.example.gym.model.dto.statistics.trainer.TrainingDetailDto;
import com.example.gym.model.subscription.SubscriptionStatus;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.trainer.TrainerPojo;
import com.example.gym.model.trainer.update.UpdateTrainerDto;
import com.example.gym.model.trainer.update.UpdateTrainerInfo;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.CreateTrainingDto;

import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.model.user.pojo.TrainerInfo;
import com.example.gym.repository.TrainingRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final UniquenessCheckService uniquenessCheckService;
    private final UserRepository userRepository;
    private final TrainingService trainingService;
    private final Mapper modelMapper;
    private final MongoTemplate mongoTemplate;
    private final TrainingRepository trainingRepository;

    public Map<String, Object> getProfitStatistics(String trainerId) {
        List<Training> trainings = trainingRepository.findAllByTrainerId(trainerId);

        Map<String, Double> profitStatistics = new HashMap<>();
        profitStatistics.put("today", 0.0);
        profitStatistics.put("this_week", 0.0);
        profitStatistics.put("this_month", 0.0);
        
        Map<String, Double> previousMonthsProfit = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue());
        LocalDate startOfMonth = today.withDayOfMonth(1);

        for (Training training : trainings) {
            LocalDate trainingDate = training.getStartTime().toLocalDate();

            double trainingProfit = training.getTrainer().getHourlyRate() * training.getClients().size() * training.getDurationInHours();

            if (trainingDate.isEqual(today)) {
                profitStatistics.put("today", profitStatistics.get("today") + trainingProfit);
            }

            if (!trainingDate.isBefore(startOfWeek)) {
                profitStatistics.put("this_week", profitStatistics.get("this_week") + trainingProfit);
            }

            if (trainingDate.getMonth() == today.getMonth() && trainingDate.getYear() == today.getYear()) {
                profitStatistics.put("this_month", profitStatistics.get("this_month") + trainingProfit);
            }

            if (trainingDate.getYear() == today.getYear() && (trainingDate.isBefore(startOfMonth) || trainingDate.getMonth() == today.getMonth())) {
                String monthYearKey = trainingDate.getYear() + "-" + trainingDate.getMonthValue();
                previousMonthsProfit.put(monthYearKey, previousMonthsProfit.getOrDefault(monthYearKey, 0.0) + trainingProfit);
                previousMonthsProfit.put("total", previousMonthsProfit.getOrDefault("total", 0.0) + trainingProfit);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("profitStatistics", profitStatistics);
        result.put("previous_months_profit", previousMonthsProfit);

        return result;
    }

    public Page<TrainingDetailDto> getTrainingsStatistics(
            String trainerId,
            int page,
            int size,
            LocalDateTime dateRangeFrom,
            LocalDateTime dateRangeTo,
            Double aboveProfit,
            Integer aboveClients
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // Получаем данные из репозитория
        Page<Training> trainingsPage = trainingRepository.findAllByTrainerIdAndEndTimeBetween(
                trainerId,
                dateRangeFrom,
                dateRangeTo,
                Pageable.unpaged() // Забираем все записи для последующей фильтрации
        );

        // Фильтрация данных
        List<TrainingDetailDto> filteredTrainings = trainingsPage.getContent().stream()
                .map(t -> {
                    Integer clientCount = t.getClients().size();
                    Double hourlyRate = t.getTrainer().getHourlyRate();
                    Double duration = t.getDurationInHours();
                    Double profit = clientCount * hourlyRate * duration;

                    LocalDate date = t.getStartTime().toLocalDate();
                    LocalTime time = t.getStartTime().toLocalTime();

                    return new TrainingDetailDto(date, time, clientCount, profit, t.getId());
                })
                .filter(dto -> (aboveProfit == null || dto.getProfit() >= aboveProfit))
                .filter(dto -> (aboveClients == null || dto.getClientCount() >= aboveClients))
                .collect(Collectors.toList());

        // Пагинация результата
        int start = Math.min(page * size, filteredTrainings.size());
        int end = Math.min((page + 1) * size, filteredTrainings.size());
        List<TrainingDetailDto> paginatedTrainings = filteredTrainings.subList(start, end);

        return new PageImpl<>(paginatedTrainings, pageable, filteredTrainings.size());
    }

    public ResponseTrainingForStatistics getTrainingStatistics(String trainerId, String trainingId) {
        ResponseTrainingForStatistics trainingStatistics = new ResponseTrainingForStatistics();

        Training training = trainingRepository.findByEndTimeBeforeAndTrainerIdAndId(
                LocalDateTime.now(),
                trainerId,
                trainingId);

        trainingStatistics.setProfit(training.getDurationInHours() * training.getClients().size() * training.getTrainer().getHourlyRate());
        trainingStatistics.setTraining(modelMapper.toDto(training));

        return trainingStatistics;
    }

    public List<ResponseTrainerWithoutTrainingsDto> findAll(
            String section,
            String name,
            String surname,
            Optional<GenderType> gender,
            LocalDateTime birthdayFrom,
            LocalDateTime birthdayBefore
    ) {
        Query query = new Query();
        
        if (section != null && !section.isEmpty() && !section.isBlank())
            query.addCriteria(Criteria.where("trainerInfo.sectionNames").in(section));
        
        if (name != null && !name.isEmpty() && !name.isBlank())
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        
        if (surname != null && !surname.isEmpty() && !surname.isBlank())
            query.addCriteria(Criteria.where("surname").regex(surname, "i"));
        
        if (gender.isPresent())
            query.addCriteria(Criteria.where("gender").is(gender.get()));
        
        if (birthdayFrom != null)
            query.addCriteria(Criteria.where("birthday").gte(birthdayFrom));
        
        if (birthdayBefore != null)
            query.addCriteria(Criteria.where("birthday").lt(birthdayBefore));

        query.addCriteria(Criteria.where("roles").in(UserRoleType.ROLE_TRAINER));
        
        List<User> trainers = mongoTemplate.find(query, User.class);
        
        return trainers.stream()
            .map(modelMapper::toDtoWithoutTraining)
            .collect(Collectors.toList());
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

        boolean needUpdate = false;

        if (dto.getName() != null && !trainer.getName().equals(dto.getName())) {
            trainer.setName(dto.getName());
            needUpdate = true;
        }

        if (dto.getSurname() != null && !trainer.getSurname().equals(dto.getSurname())) {
            trainer.setSurname(dto.getSurname());
            needUpdate = true;
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

        if (dto.getGender() != null && trainer.getGender() != dto.getGender()) {
            trainer.setGender(dto.getGender());
            needUpdate = true;
        }

        if (dto.getBirthday() != null && (trainer.getBirthday() == null || 
                (trainer.getBirthday() != null && !trainer.getBirthday().equals(dto.getBirthday())))) {
            trainer.setBirthday(dto.getBirthday());
        }

        TrainerInfo trainerInfo = trainer.getTrainerInfo();
        UpdateTrainerInfo updateTrainerDto = dto.getTrainerInfo();
        if (updateTrainerDto.getExperience() != null && trainerInfo.getExperience() != updateTrainerDto.getExperience()) {
            trainerInfo.setExperience(updateTrainerDto.getExperience());
            needUpdate = true;
        }

        if (updateTrainerDto.getQualification() != null && trainerInfo.getQualification() != updateTrainerDto.getQualification()) {
            trainerInfo.setQualification(updateTrainerDto.getQualification());
            needUpdate = true;
        }

        if (updateTrainerDto.getHourlyRate() != null && trainerInfo.getHourlyRate() != updateTrainerDto.getHourlyRate()) {
            trainerInfo.setHourlyRate(updateTrainerDto.getHourlyRate());
            needUpdate = true;
        }

        if (updateTrainerDto.getSections() != null) {
            trainerInfo.setSections(updateTrainerDto.getSections());
        }

        trainer.setTrainerInfo(trainerInfo);

        User updatedTrainer = userRepository.save(trainer);

        if (needUpdate) {
            List<Training> trainings = trainingRepository.findAllByTrainerId(updatedTrainer.getId());
            trainings.forEach(training -> {
                TrainerPojo trainerPojo = new TrainerPojo();
                trainerPojo.setId(updatedTrainer.getId());
                trainerPojo.setName(updatedTrainer.getName());
                trainerPojo.setSurname(updatedTrainer.getSurname());
                trainerPojo.setGender(updatedTrainer.getGender());
                trainerPojo.setQualification(updatedTrainer.getTrainerInfo().getQualification());
                trainerPojo.setHourlyRate(updatedTrainer.getTrainerInfo().getHourlyRate());
                training.setTrainer(trainerPojo);
                trainingRepository.save(training);
            });
        }

        return modelMapper.toTrainerDto(updatedTrainer);
    } 

    public void deleteTrainer(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && user.get().getClientInfo() != null &&  
                !user.get().getClientInfo().getSubscriptions().isEmpty() &&
                user.get().getClientInfo().getSubscriptions().get(0).getStatus() == SubscriptionStatus.ACTIVE) {
            throw new InvalidDataException("Нельзя удалить пользователя с активным абонементом");
        } else {
            userRepository.deleteById(id);
        }
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

    public List<ResponseTrainingDto> findTrainigs(String id) {
        getById(id);
        return trainingService.findActualTrainingsByTrainerId(id);
    }

    @Transactional
    public ResponseTrainingDto createTraining(CreateTrainingDto dto, String trainerId) throws ResourceNotFoundException, InvalidDataException {
        User trainer = getById(trainerId);

        LocalDateTime newStartTime = dto.getStartTime();
        LocalDateTime newEndTime = dto.getEndTime();

        if (newStartTime.isAfter(newEndTime)) {
            throw new InvalidDataException("Время новой тренировки пересекается с существующей тренировкой.");
        }
    
        List<Training> overlappingTrainings = trainingService.findAll().stream()
                .filter(training -> training.getTrainer().getId().equals(trainerId))
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
