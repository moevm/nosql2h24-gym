package com.example.gym.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.room.CreateRoomDto;
import com.example.gym.model.room.ResponseRoomDto;
import com.example.gym.model.room.UpdateRoomDto;
import com.example.gym.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(name = "Room", description = "Операции, связанные с залами")
@Validated
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "Создать зал.",
            description = "Создает зал.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Зал успешно создан.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseRoomDto.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Неккоректные данные.",
                        content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> createRoom(
            @RequestBody @Parameter(description = "Сущность зала, который необходимо создать", required = true)
            @Valid CreateRoomDto dto
    ) {
        ResponseRoomDto createdRoom = roomService.create(dto);
        return ResponseEntity.created(URI.create("/rooms/" + createdRoom.getId())).body(createdRoom);
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Обновить зал по его идентификатору.",
            description = "Обновляет зал по его идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Зал успешно обновлен.",
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
                            description = "Зал с указанным id не найден.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> updateRoom(
            @Parameter(description = "Идентификатор зала, который необходимо обновить.", required = true)
            @PathVariable String roomId,
            @RequestBody @Parameter(description = "Сущность зала, для обновления существующего", required = true)
            @Valid UpdateRoomDto updateRoomDto
    ) {
        ResponseRoomDto updatedRoom = roomService.update(roomId, updateRoomDto);
        return ResponseEntity.ok().body(updatedRoom);
    }

    @GetMapping
    @Operation(summary = "Получить все залы",
            description = "Возвращает все залы.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Список залов успешно получен.",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(
                                        schema = @Schema(implementation = ResponseRoomDto.class) 
                                ))
                )
    })  
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(roomService.findAll());
    }
    
    @GetMapping("/{roomId}")
    @Operation(summary = "Получить зал по его идентификатору.",
            description = "Получить зал по его идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Зал успешно обновлен.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseRoomDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Зал с указанным id не найден.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> getRoomById(
            @Parameter(description = "Идентификатор зала, который необходимо обновить.", required = true)
            @PathVariable String roomId
    ) {
        return ResponseEntity.ok().body(roomService.getRoomById(roomId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить зал по его идентификатору.",
            description = "Удаляет зал по его идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Зал успешно удален.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    )
    })
    public ResponseEntity<?> deleteRoom(
            @PathVariable @Parameter(description = "Идентификатор зала, который необходимо удалить", required = true)
            String id
    ) {
        roomService.delete(id);
        return ResponseEntity.ok().body(null);
    }

}
