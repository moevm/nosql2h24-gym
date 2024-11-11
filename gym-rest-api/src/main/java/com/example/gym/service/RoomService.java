package com.example.gym.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.model.room.CreateRoomDto;
import com.example.gym.model.room.LocationPojo;
import com.example.gym.model.room.ResponseRoomDto;
import com.example.gym.model.room.Room;
import com.example.gym.repository.RoomRepository;
import com.example.gym.util.Mapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final Mapper modelMapper;

    public ResponseRoomDto create(CreateRoomDto createRoomDto) {
        Room room = modelMapper.toModel(createRoomDto);
        room.setLocation(new LocationPojo(createRoomDto.getAddress(), createRoomDto.getNumber()));
        Room savedRoom = roomRepository.save(room);
        return modelMapper.toDto(savedRoom);
    }

    public List<ResponseRoomDto> findAll() {
        return roomRepository.findAll().stream()
                .map(modelMapper::toDto)
                .toList();
    }

    public void delete(String roomId) {
        roomRepository.deleteById(roomId);
    }

}
