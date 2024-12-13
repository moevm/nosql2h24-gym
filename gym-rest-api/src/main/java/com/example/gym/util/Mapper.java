package com.example.gym.util;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.dto.statistics.admin.SubscriptionDetailDto;
import com.example.gym.model.dto.statistics.trainer.ResponseTrainingForStatistics;
import com.example.gym.model.promotion.Promotion;
import com.example.gym.model.promotion.dto.CreatePromotionDto;
import com.example.gym.model.promotion.dto.ResponsePromotionDto;
import com.example.gym.model.room.CreateRoomDto;
import com.example.gym.model.room.ResponseRoomDto;
import com.example.gym.model.room.Room;
import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.section.dto.CreateSectionDto;
import com.example.gym.model.section.dto.ResponseSectionDto;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.trainer.TrainerPojo;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingForClientDto;
import com.example.gym.model.user.ResponseUserDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.Section;
import com.example.gym.model.user.pojo.Subscription;

@Service
public class Mapper {

    private final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        this.modelMapper.addMappings(new PropertyMap<CreateTrainingDto, Training>() {
            @Override
            protected void configure() {
                map().setId(null);
            }
        });

        this.modelMapper.addMappings(new PropertyMap<CreatePromotionDto, Promotion>() {
            @Override
            protected void configure() {
                map().setId(null);
            }
        });

    }

    public ResponseAdminDto toAdminDto(User admin) {
        return modelMapper.map(admin, ResponseAdminDto.class);
    }

    public ResponseClientDto toClientDto(User client) {
        ResponseClientDto responseClientDto = modelMapper.map(client, ResponseClientDto.class);
        responseClientDto.setClientInfo(client.getClientInfo());
        return responseClientDto;
    }

    public ResponseTrainerWithoutTrainingsDto toDtoWithoutTraining(User trainer) {
        ResponseTrainerWithoutTrainingsDto trainerDto = modelMapper.map(trainer, ResponseTrainerWithoutTrainingsDto.class);
        trainerDto.setSections(trainer.getTrainerInfo().getSections());
        if (trainer.getTrainerInfo() != null) {
            trainerDto.setQualification(trainer.getTrainerInfo().getQualification());
            trainerDto.setHourlyRate(trainer.getTrainerInfo().getHourlyRate());
            trainerDto.setExperience(trainer.getTrainerInfo().getExperience());
        }
        return trainerDto;
    }

    public ResponseTrainerDto toTrainerDto(User trainer) {
        ResponseTrainerDto trainerDto = modelMapper.map(trainer, ResponseTrainerDto.class);
        trainerDto.setTrainerInfo(trainer.getTrainerInfo());
        return trainerDto;
    }

    public Section toModel(CreateSectionDto createSectionDto) {
        return modelMapper.map(createSectionDto, Section.class);
    }

    public Promotion toModel(CreatePromotionDto createPromotionDto) {
        return modelMapper.map(createPromotionDto, Promotion.class);
    }

    public Room toModel(CreateRoomDto createRoomDto) {
        return modelMapper.map(createRoomDto, Room.class);
    }

    public ResponsePromotionDto toDto(Promotion promotion) {
        ResponsePromotionDto dto = modelMapper.map(promotion, ResponsePromotionDto.class);
        dto.setCreatedBy(promotion.getCreatedBy());
        return dto;
    }

    public ResponseSectionDto toSectionDto(Section section) {
        return modelMapper.map(section, ResponseSectionDto.class);
    }


    public ResponseTrainingDto toDto(Training training) {
        ResponseTrainingDto dto = modelMapper.map(training, ResponseTrainingDto.class);
        dto.setTrainer(training.getTrainer());
        dto.setClients(training.getClients());
        dto.setRoom(training.getRoom());
        return dto;
    }

    public ResponseRoomDto toDto(Room room) {
        ResponseRoomDto dto = modelMapper.map(room, ResponseRoomDto.class);
        if (room.getTrainers() != null) {
            dto.setTrainers(room.getTrainers().stream().map(t -> t.getId()).toList());
        } else {
            dto.setTrainers(new ArrayList<>());
        }
        dto.setLocation(room.getLocation());
        return dto;
    }

    public Training toModel(CreateTrainingDto training) {
        Training trainingModel = modelMapper.map(training, Training.class);
        trainingModel.setStartTime(training.getStartTime());
        trainingModel.setEndTime(training.getEndTime());
        trainingModel.setSection(new Section(training.getSection()));
        
        return trainingModel;
    }

    public TrainerPojo toPojo(User trainer) {
        TrainerPojo trainerPojo = modelMapper.map(trainer, TrainerPojo.class);
        trainerPojo.setHourlyRate(trainer.getTrainerInfo().getHourlyRate());
        trainerPojo.setQualification(trainer.getTrainerInfo().getQualification());
        return trainerPojo;
    }

    public RoomPojo toPojo(Room room) {
        RoomPojo roomPojo = new RoomPojo(room.getId(), room.getName(), room.getCapacity());
        return roomPojo;
    }

    public ClientPojo toClientPojo(User newClient) {
        ClientPojo client = modelMapper.map(newClient, ClientPojo.class);
        client.setRegistrationDate(newClient.getCreatedAt());
        client.setLoyaltyPoints(newClient.getClientInfo().getLoyaltyPoints());
        return client;
    }

    public ResponseSubscriptionDto toDto(Subscription subscription, String clientId) {
        ResponseSubscriptionDto responseSubscriptionDto = modelMapper.map(subscription, ResponseSubscriptionDto.class);
        responseSubscriptionDto.setClientId(clientId);
        long totalDays = ChronoUnit.DAYS.between(subscription.getStartDate(), subscription.getEndDate());
        responseSubscriptionDto.setDuration(totalDays);
        return responseSubscriptionDto;
    }

    public SubscriptionDetailDto toDto(Subscription subscription, User client) {
        SubscriptionDetailDto subscriptionDto = modelMapper.map(subscription, SubscriptionDetailDto.class);
        subscriptionDto.setClient(toClientPojo(client));
        return subscriptionDto;
    }

    public ResponseTrainingForClientDto toTrainingForClientDto(Training training) {
        ResponseTrainingForClientDto trainingForClientDto = modelMapper.map(training, ResponseTrainingForClientDto.class);
        long hours = ChronoUnit.HOURS.between(training.getStartTime(), training.getEndTime());
        trainingForClientDto.setDuration(hours);
        return trainingForClientDto;
    }

    public ResponseUserDto toDto(User user) {
        return modelMapper.map(user, ResponseUserDto.class);
    }

    public TrainerPojo toTrainerPojo(User user) {
        TrainerPojo trainerPojo = modelMapper.map(user, TrainerPojo.class);
        trainerPojo.setHourlyRate(user.getTrainerInfo().getHourlyRate());
        trainerPojo.setQualification(user.getTrainerInfo().getQualification());
        return trainerPojo;
    }

    public ResponseTrainingForStatistics toStatisticsDto(Training training) {
        ResponseTrainingDto responseTrainingDto = toDto(training);

        ResponseTrainingForStatistics trainingForStatistics = new ResponseTrainingForStatistics();
        trainingForStatistics.setTraining(responseTrainingDto);
        trainingForStatistics.setProfit(training.getDurationInHours() * training.getClients().size() * training.getTrainer().getHourlyRate());

        return trainingForStatistics;
    }

}
