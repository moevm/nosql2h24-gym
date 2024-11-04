package com.example.gym.model.training;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.gym.model.room.Room;
import com.example.gym.model.section.Section;
import com.example.gym.model.user.User;

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
    private Float duration;

    @DBRef(lazy = false)
    private User trainer;

    @DBRef(lazy = false)
    private Section section;

    @DBRef(lazy = false)
    private Room room;

    @DBRef(lazy = true)
    private Set<User> clients = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
