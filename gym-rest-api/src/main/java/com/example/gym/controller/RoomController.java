package com.example.gym.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.room.CreateRoomDto;
import com.example.gym.model.room.ResponseRoomDto;
import com.example.gym.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "Добавить тренировку по идентификатору тренера.",
                description = "Добавить тренировку по идентификатору тренера.",
                responses = {
                        @ApiResponse(
                                responseCode = "201",
                                description = "Тренировка успешно создана.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseRoomDto.class))
                        ),
                        @ApiResponse(
                            responseCode = "400",
                            description = "Неккоректные данные.",
                            content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseError.class))
                        ),
                        @ApiResponse(
                                responseCode = "404",
                                description = "Тренер с указанным идентификатором не найден.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseError.class))
                        )
    })
    public ResponseEntity<?> createRoom(
            @RequestBody @Parameter(description = "Сущность зала, который необходимо создать", required = true)
            CreateRoomDto dto
    ) {
        ResponseRoomDto createdRoom = roomService.create(dto);
        return ResponseEntity.created(URI.create("/rooms/" + createdRoom.getId())).body(createdRoom);
    }

    @GetMapping
    @Operation(summary = "Получить все комнаты",
                description = "Возвращает все комнаты.",
                responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список комнат успешно получен.",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ResponseRoomDto.class) 
                                    ))
                    )
    })  
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(roomService.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить комнтаы по её идентификатору.",
                    description = "Удаляет комнату по её идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Комната успешно удалена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = String.class))
                            )
    })
    public ResponseEntity<?> deleteTrainer(
            @PathVariable @Parameter(description = "Идентификатор комнаты, которую необходимо удалить", required = true)
            String id
    ) {
        roomService.delete(id);
        return ResponseEntity.ok().body(null);
    }

}
