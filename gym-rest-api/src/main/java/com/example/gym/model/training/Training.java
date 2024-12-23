package com.example.gym.model.training;

import java.time.Duration;
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
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "training_sessions")
@Builder
public class Training {

    @Id
    @JsonProperty("_id")
    private String id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean hasFreeRegistration;
    private Integer availableSlots;

    @JsonProperty("trainer")
    private TrainerPojo trainer;
    @JsonProperty("section")
    private Section section;
    @JsonProperty("room")
    private RoomPojo room;

    private TrainingStatus status;

    @JsonProperty("cleints")
    private List<ClientPojo> clients = new ArrayList<>();

    @CreatedDate

    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Double getDurationInHours() {
        Duration duration = Duration.between(startTime, endTime);
        return (double) duration.toHoursPart() + (double) duration.toMinutesPart() / 60;
    }

    public boolean isFree() {
        return hasFreeRegistration && availableSlots > 0;
    }

}
