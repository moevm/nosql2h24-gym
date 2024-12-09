package com.example.gym.model.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseRoomDto {

    private String id;
    private String name;
    private String capacity;
    private LocationPojo location;
    
}
