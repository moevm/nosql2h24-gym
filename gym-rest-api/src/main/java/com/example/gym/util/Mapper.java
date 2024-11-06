package com.example.gym.util;

import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.section.Section;
import com.example.gym.model.section.dto.CreateSectionDto;
import com.example.gym.model.section.dto.ResponseSectionDto;
import com.example.gym.model.subscription.Subscription;
import com.example.gym.model.subscription.dto.CreateSubscriptionDto;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.training.Training;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingForClientDto;
import com.example.gym.model.user.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

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

        this.modelMapper.addMappings(new PropertyMap<CreateSubscriptionDto, Subscription>() {
            @Override
            protected void configure() {
                map().setId(null);
            }
        });

        this.modelMapper.addMappings(new PropertyMap<Subscription, ResponseSubscriptionDto>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setClientId(source.getClient().getId());
                map().setRestDays(source.getRestDays());
            }
        });
    }

    public ResponseAdminDto toAdminDto(User admin) {
        return modelMapper.map(admin, ResponseAdminDto.class);
    }

    public ResponseClientDto toClientDto(User client) {
        return modelMapper.map(client, ResponseClientDto.class);
    }

    public ResponseTrainerWithoutTrainingsDto toDtoWithoutTraining(User trainer) {
        ResponseTrainerWithoutTrainingsDto trainerDto = modelMapper.map(trainer, ResponseTrainerWithoutTrainingsDto.class);
        trainerDto.setSections(trainer.getSectionNames());
        return trainerDto;
    }

    public ResponseTrainerDto toTrainerDto(User trainer) {
        ResponseTrainerDto trainerDto = modelMapper.map(trainer, ResponseTrainerDto.class);
        trainerDto.setSections(trainer.getSectionNames());
        return trainerDto;
    }

    public ResponseTrainingForClientDto toDtoWithTrainer(Training training) {
        ResponseTrainingForClientDto trainingDto = modelMapper.map(training, ResponseTrainingForClientDto.class);
        User trainer = training.getTrainer();
        ResponseTrainerWithoutTrainingsDto trainerDto = this.toDtoWithoutTraining(trainer);
        trainerDto.setSections(trainer.getSectionNames());
        trainingDto.setTrainer(trainerDto);
        return trainingDto;
    }


    public Section toModel(CreateSectionDto createSectionDto) {
        return modelMapper.map(createSectionDto, Section.class);
    }

    public ResponseSectionDto toSectionDto(Section section) {
        return modelMapper.map(section, ResponseSectionDto.class);
    }


    public ResponseTrainingDto toDto(Training schedule) {
        ResponseTrainingDto dto = modelMapper.map(schedule, ResponseTrainingDto.class);
        dto.setTrainerId(schedule.getTrainer().getId());
        return dto;
    }

    public Training toModel(CreateTrainingDto training) {
        return modelMapper.map(training, Training.class);
    }

    public ResponseSubscriptionDto toDto(Subscription subscription) {
        return modelMapper.map(subscription, ResponseSubscriptionDto.class);
    }

    public Subscription toModel(CreateSubscriptionDto subscription) {
        return modelMapper.map(subscription, Subscription.class);
    }

}
