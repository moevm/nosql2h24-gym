package com.example.gym.model.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.gym.model.section.Section;
import com.example.gym.model.subscription.Subscription;
import com.example.gym.model.training.Training;

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
@Document(collection = "my_users")
@ToString
public class User {

    @Id
    private String id;

    private String name;
    private String surname;
    private String password;
    private String email;
    private String phoneNumber;
    private String comment;
    private Integer experience;
    private Integer hourlyRate = 0;
    private String qualification;
    private boolean free = true;

    @DBRef(lazy = true)
    private Set<UserRole> roles = new HashSet<>();

    private Integer roleIndex;

    @DBRef(lazy = true)
    private Set<Training> trainings;

    @DBRef(lazy = true)
    private Subscription subscription;

    @DBRef(lazy = true)
    private Set<Section> sections = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public List<String> getSectionNames() {
        return sections.stream()
                .map(s -> s.getName())
                .toList();
    }

}
