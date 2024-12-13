package com.example.gym.model.trainer;

import com.example.gym.model.user.pojo.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainerPojo {

    private String id;
    private String name;
    private String surname;
    private GenderType gender;
    private String qualification;
    private Double hourlyRate;

}
