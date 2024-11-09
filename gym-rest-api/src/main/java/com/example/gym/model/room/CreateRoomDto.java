package com.example.gym.model.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateRoomDto {

    private String name;
    private Integer capacity;
    private String address;
    private Integer number;

}
