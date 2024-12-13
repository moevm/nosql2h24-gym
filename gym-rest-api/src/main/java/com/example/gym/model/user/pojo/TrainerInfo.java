package com.example.gym.model.user.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainerInfo {

    private Integer experience;
    private Double hourlyRate = 0.0;
    private String qualification;
    private boolean free = true;
    private List<String> sections;

}
