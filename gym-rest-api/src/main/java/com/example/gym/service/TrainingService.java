package com.example.gym.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.gym.model.training.Training;
import com.example.gym.model.training.TrainingStatus;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.training.dto.UpdateTrainingDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.model.user.pojo.Section;
import com.example.gym.repository.RoomRepository;
import com.example.gym.repository.TrainingRepository;
import com.example.gym.security.service.MyUserDetail;
import com.example.gym.security.service.MyUserDetailService;
import com.example.gym.util.Mapper;
import com.example.gym.exception.InvalidDataException;
import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.room.Room;
import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.trainer.TrainerPojo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final ClientService clientService;
    private final Mapper modelMapper;
    private final RoomRepository roomRepository;
    private final MongoTemplate mongoTemplate;
    private final MyUserDetailService myUserDetailService;

    public ResponseTrainingDto findTrainingById(String id) throws ResourceNotFoundException {
        Training training = getById(id);
        return modelMapper.toDto(training);
    }

    
    public List<ResponseTrainingDto> findTrainingsByTrainerId(String trainerId) {
        List<Training> trainings = trainingRepository.findAllByTrainerId(trainerId);
        return trainings.stream()
                .map(s -> modelMapper.toDto(s))
                .toList();
    }

    // @Transactional
    // public ResponseTrainingDto start(String trainingId) {
    //     Training training = getById(trainingId);
    //     training.setStatus(TrainingStatus.PROGRESS);
    //     Training savedTraining = trainingRepository.save(training);
    //     return modelMapper.toDto(savedTraining);
    // }

    // @Transactional
    // public ResponseTrainingDto finish(String trainingId) {
    //     Training training = getById(trainingId);
    //     training.setStatus(TrainingStatus.FINISH);
    //     Training savedTraining = trainingRepository.save(training);

    //     List<ClientPojo> clientPojos = training.getClients();
    //     List<String> clientIds = clientPojos.stream()
    //             .map(ClientPojo::getId)
    //             .toList();
        
    //     List<User> clients = userRepository.findAllById(clientIds);
        
    //     List<LoyaltySettings> loyaltySettings = loyaltySettingsRepository.findAll();

    //     if (!loyaltySettings.isEmpty()) {
    //         LoyaltySettings loyaltySetting = loyaltySettings.get(0);

    //         long trainingDuration = ChronoUnit.HOURS.between(training.getStartTime(), training.getEndTime());
    //         TrainerPojo trainer = training.getTrainer();
    //         Double trainerHourlyRate = trainer.getHourlyRate();

    //         clients.stream().forEach(u -> {

    //             Double loyalty = (trainerHourlyRate * trainingDuration * loyaltySetting.getAcceptanceRate());
    //             Integer bonus = 0;
    //             if (loyalty < 1d) {
    //                 bonus = 1;
    //             } else {
    //                 bonus = (int) Math.ceil(loyalty);
    //             }

    //             ClientInfo clientInfo = u.getClientInfo();

    //             if (clientInfo != null) {
    //                 if (clientInfo.getLoyaltyPoints() == null)
    //                     clientInfo.setLoyaltyPoints(0);

    //                 clientInfo.setLoyaltyPoints(clientInfo.getLoyaltyPoints() + bonus);
    //             }

    //             u.setClientInfo(clientInfo);
    //             userRepository.save(u);
    //         });
    //     }

    //     return modelMapper.toDto(savedTraining);
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
    public ResponseTrainingDto createTraining(CreateTrainingDto dto, User trainer) throws InvalidDataException {
        LocalDateTime newStartTime = dto.getStartTime();
        LocalDateTime newEndTime = dto.getEndTime();
        validateTime(newStartTime, newEndTime);
        validateOverlapping(newStartTime, newEndTime, trainer.getId());

        Training training = modelMapper.toModel(dto);
        training.setStartTime(dto.getStartTime());
        training.setEndTime(dto.getEndTime());
        TrainerPojo trainerPojo = modelMapper.toPojo(trainer);
        training.setTrainer(trainerPojo);
        training.setStatus(TrainingStatus.AWAIT);
        training.setHasFreeRegistration(true);

        Optional<Room> optionalRoom = roomRepository.findById(dto.getRoomId());
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            RoomPojo roomPojo = modelMapper.toPojo(room);
            System.out.println(roomPojo.getName());
            training.setRoom(roomPojo);
        }

        Training createdTraining = trainingRepository.save(training);
        return modelMapper.toDto(createdTraining);
    }

    public void deleteTraining(String trainingId) {
        trainingRepository.deleteById(trainingId);
    }

    @Transactional
    public void registrationClientForTraining(String trainingId, String clientId) throws ResourceNotFoundException, InvalidDataException {
        Training training = getById(trainingId);
        User client = clientService.getById(clientId);

        if (!training.isFree()) {
            throw new InvalidDataException("Свободных записей нет");
        }

        List<ClientPojo> clientsInTraining = training.getClients();

        if (clientsInTraining.stream().anyMatch(c -> c.getId().equals(clientId))) {
            throw new InvalidDataException("Клиент уже записан на эту тренировку");
        }

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
    public ResponseTrainingDto updateTraining(
            String trainingId, 
            UpdateTrainingDto dto
    ) throws ResourceNotFoundException, InvalidDataException {
        Training training = getById(trainingId);

        if (dto.getStartTime() != null && !training.getStartTime().equals(dto.getStartTime())) {
            validateTime(dto.getStartTime(), training.getEndTime());
            training.setStartTime(dto.getStartTime());
        }

        if (dto.getEndTime() != null && !training.getEndTime().equals(dto.getEndTime())) {
            validateTime(training.getStartTime(), dto.getEndTime());
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

    public List<ClientPojo> findTrainingClients(String trainingId) throws ResourceNotFoundException {
        Training training = getById(trainingId);
        return training.getClients();
    }

    // public ResponseTrainingForStatistic findAllConductedTrainings() {
    //     ResponseTrainingForStatistic trainingsForStatistic = new ResponseTrainingForStatistic();
    //     List<ResponseTrainingDto> trainings = findAll().stream()
    //             .map(t -> modelMapper.toDto(t))
    //             .toList();
    //     trainingsForStatistic.setCount(trainings.size());
    //     trainingsForStatistic.setTrainings(trainings);
    //     return trainingsForStatistic;
    // }

    public List<ResponseTrainingDto> findAllTrainigs(
            String address,
            String section,
            String name,
            String surname,
            Optional<GenderType> gender,
            LocalDateTime startTime,
            Principal principal
    ) {

        MyUserDetail user = myUserDetailService.loadUserByUsername(principal.getName());
    
        Query query = new Query();

        if (section != null && !section.isEmpty() && !section.isBlank()) {
            query.addCriteria(Criteria.where("section.name").regex(section, "i"));
        }

        if (name != null && !name.isEmpty() && !name.isBlank()) {
            query.addCriteria(Criteria.where("trainer.name").regex(name, "i"));
        }

        if (surname != null && !surname.isEmpty() && !surname.isBlank()) {
            query.addCriteria(Criteria.where("trainer.surname").regex(surname, "i"));
        }

        if (gender.isPresent()) {
            query.addCriteria(Criteria.where("trainer.gender").is(gender.get()));
        }

        if (startTime != null) {
            query.addCriteria(Criteria.where("startTime").gte(startTime));
        }

        String userId = user.getId();
        query.addCriteria(Criteria.where("clients").not().elemMatch(Criteria.where("id").is(userId)));

        List<Training> trainings = mongoTemplate.find(query, Training.class);

        if (address != null && !address.isEmpty() && !address.isBlank()) {
            Query roomQuery = new Query();
            roomQuery.addCriteria(Criteria.where("location.address").regex(address, "i"));
            List<Room> rooms = mongoTemplate.find(roomQuery, Room.class);
            List<String> roomIds = rooms.stream().map(Room::getId).collect(Collectors.toList());
            trainings = trainings.stream()
                    .filter(t -> roomIds.contains(t.getRoom().getId()))
                    .collect(Collectors.toList());
        }

        return trainings.stream()
            .map(t -> modelMapper.toDto(t))
            .toList();
    }

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    public Training getById(String id) throws ResourceNotFoundException {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тренировка с id %s не найдена".formatted(id)));
    }

    private void validateTime(LocalDateTime startTime, LocalDateTime endTime) throws InvalidDataException {
        if (startTime.isAfter(endTime)) {
            throw new InvalidDataException("Конец тренировки не может быть раньше начала.");
        }
    }

    private void validateOverlapping(LocalDateTime startTime, LocalDateTime endTime, String trainerId) throws InvalidDataException {
        List<Training> overlappingTrainings = findAll().stream()
                .filter(training -> training.getTrainer().getId().equals(trainerId))
                .filter(training -> 
                    (startTime.isBefore(training.getEndTime()) && endTime.isAfter(training.getStartTime()))
                )
                .collect(Collectors.toList());
        
        if (!overlappingTrainings.isEmpty()) {
            throw new InvalidDataException("Время новой тренировки пересекается с существующей тренировкой.");
        }
    }

}
