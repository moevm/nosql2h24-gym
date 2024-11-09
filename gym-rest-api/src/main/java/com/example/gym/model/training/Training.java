package com.example.gym.model.training;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.trainer.TrainerPojo;
import com.example.gym.model.user.pojo.Section;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "training_sessions")
public class Training {

    @Id
    private String id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean hasFreeRegistration = true;
    private Integer availableSlots;

    private TrainerPojo trainerPojo;
    private Section section;
    private RoomPojo room;

    private List<ClientPojo> clients = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
