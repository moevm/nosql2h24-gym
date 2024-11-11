package com.example.gym.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.promotion.Promotion;
import com.example.gym.model.promotion.dto.CreatePromotionDto;
import com.example.gym.model.promotion.dto.ResponsePromotionDto;
import com.example.gym.model.room.CreateRoomDto;
import com.example.gym.model.room.ResponseRoomDto;
import com.example.gym.model.room.Room;
import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.section.dto.CreateSectionDto;
import com.example.gym.model.section.dto.ResponseSectionDto;
import com.example.gym.model.subscription.dto.CreateSubscriptionDto;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.trainer.TrainerPojo;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingForClientDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.Section;

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
        trainerDto.setSections(trainer.getTrainerInfo().getSectionNames());
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
        dto.setTrainer(training.getTrainerPojo());
        dto.setClients(training.getClients());
        dto.setRoom(training.getRoom());
        return dto;
    }

    public ResponseRoomDto toDto(Room room) {
        ResponseRoomDto dto = modelMapper.map(room, ResponseRoomDto.class);
        dto.setLocation(room.getLocation());
        return dto;
    }

    public Training toModel(CreateTrainingDto training) {
        Training trainingModel = modelMapper.map(training, Training.class);
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
        RoomPojo roomPojo = modelMapper.map(room, RoomPojo.class);
        return roomPojo;
    }

    public ClientPojo toClientPojo(User newClient) {
        ClientPojo client = modelMapper.map(newClient, ClientPojo.class);
        client.setRegistrationDate(newClient.getCreatedDate());
        client.setLoyaltyPoints(newClient.getClientInfo().getLoyaltyPoints());
        return client;
    }

}
