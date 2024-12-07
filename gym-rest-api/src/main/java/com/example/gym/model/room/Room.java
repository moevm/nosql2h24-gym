package com.example.gym.model.room;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.gym.model.trainer.TrainerPojo;
import com.example.gym.model.user.pojo.Section;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "rooms")
public class Room {

    @Id
    @JsonProperty("_id")
    private String id;
    private String name;
    private Integer capacity;
    @JsonProperty("location")
    private LocationPojo location;
    private String workingDays;
    private LocalTime openingTime;
    private LocalTime closingTime;
    @JsonProperty("trainers")
    private List<TrainerPojo> trainers;
    @JsonProperty("sections")
    private List<String> sections;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
