package com.example.gym.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.model.room.CreateRoomDto;
import com.example.gym.model.room.LocationPojo;
import com.example.gym.model.room.ResponseRoomDto;
import com.example.gym.model.room.Room;
import com.example.gym.model.room.RoomPojo;
import com.example.gym.model.room.UpdateRoomDto;
import com.example.gym.model.training.Training;
import com.example.gym.model.user.User;
import com.example.gym.repository.RoomRepository;
import com.example.gym.repository.TrainingRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final Mapper modelMapper;
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    public ResponseRoomDto create(CreateRoomDto createRoomDto) {
        Room room = modelMapper.toModel(createRoomDto);
        room.setLocation(new LocationPojo(createRoomDto.getLocation().getAddress(), createRoomDto.getLocation().getNumber()));

        List<User> trainers = userRepository.findAllById(createRoomDto.getTrainers());

        room.setTrainers(trainers.stream()
                .map(modelMapper::toTrainerPojo)
                .toList());
        Room savedRoom = roomRepository.save(room);
        return modelMapper.toDto(savedRoom);
    }

    public ResponseRoomDto update(String id, UpdateRoomDto updateRoomDto) {
        Room room = getById(id);

        if (updateRoomDto.getName() != null && !room.getName().equals(updateRoomDto.getName())) {
            room.setName(updateRoomDto.getName());
        }

        if (updateRoomDto.getCapacity() != null && !room.getCapacity().equals(updateRoomDto.getCapacity())) {
            room.setCapacity(updateRoomDto.getCapacity());
        }

        if (updateRoomDto.getLocation().getAddress() != null && !room.getLocation().getAddress().equals(updateRoomDto.getLocation().getAddress())) {
            room.getLocation().setAddress(updateRoomDto.getLocation().getAddress());
        }
    
        if (updateRoomDto.getLocation().getNumber() != null && !room.getLocation().getNumber().equals(updateRoomDto.getLocation().getNumber())) {
            room.getLocation().setNumber(updateRoomDto.getLocation().getNumber());
        }

        if (updateRoomDto.getClosingTime() != null && !updateRoomDto.getClosingTime().equals(room.getClosingTime())) {
            room.setClosingTime(updateRoomDto.getClosingTime());
        }

        if (updateRoomDto.getOpeningTime() != null && !updateRoomDto.getOpeningTime().equals(room.getOpeningTime())) {
            room.setOpeningTime(updateRoomDto.getOpeningTime());
        }

        if (updateRoomDto.getTrainers() != null && updateRoomDto.getTrainers().size() != 0) {
            List<User> trainers = userRepository.findAllById(updateRoomDto.getTrainers());

            room.setTrainers(trainers.stream()
                    .map(modelMapper::toTrainerPojo)
                    .toList());
        }

        if (updateRoomDto.getSections() != null && updateRoomDto.getSections().size() != 0) {
            room.setSections(updateRoomDto.getSections());
        }

        if (updateRoomDto.getWorkingDays() != null) {
            room.setWorkingDays(updateRoomDto.getWorkingDays());
        }

        Room updatedRoom = roomRepository.save(room);

        List<Training> trainings = trainingRepository.findAllByRoomId(room.getId());
        trainings.forEach(training -> {
            RoomPojo roomPojo = training.getRoom();
            
            if (roomPojo != null) {
                if (updateRoomDto.getName() != null && !roomPojo.getName().equals(updatedRoom.getName())) {
                    roomPojo.setName(room.getName());
                }
                
                if (updateRoomDto.getCapacity() != null && !roomPojo.getCapacity().equals(updatedRoom.getCapacity())) {
                    roomPojo.setCapacity(room.getCapacity());
                }
                
                trainingRepository.save(training);
            }
        });

        return modelMapper.toDto(updatedRoom);
    }

    public List<ResponseRoomDto> findAll() {
        return roomRepository.findAll().stream()
                .map(modelMapper::toDto)
                .toList();
    }

    public void delete(String roomId) {
        List<Training> trainings = trainingRepository.findAllByRoomId(roomId);
        trainings.forEach(t -> {
                t.setRoom(null); 
                trainingRepository.save(t);
        });
        
        roomRepository.deleteById(roomId);
    }

    public ResponseRoomDto getRoomById(String id) throws ResourceNotFoundException {
        return modelMapper.toDto(getById(id));

    }

    public Room getById(String id) throws ResourceNotFoundException {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Зал с id %s не найден".formatted(id)));

    }

}
