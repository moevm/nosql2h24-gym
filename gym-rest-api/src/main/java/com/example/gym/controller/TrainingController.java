package com.example.gym.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.exception.InvalidDataException;
import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.model.client.ClientPojo;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingClientDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.service.TrainingService;
import com.example.gym.util.ResponseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Tag(name = "Training", description = "Операции, связанные с тренировками")
public class TrainingController {

    private final TrainingService trainingService;
    private final ResponseService responseService;

    @DeleteMapping("/{trainingId}")
    @Operation(summary = "Удалить тренировку по её идентификатору",
                    description = "Удаляет тренировку по её идентификатору",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренировка успешно удалена."
                            )
    })
    public ResponseEntity<?> deleteTraining(
            @Parameter(description = "Идентификатор тренировки, которую необходимо удалить.", required = true)
            @PathVariable String trainingId
    ) {
        trainingService.deleteTraining(trainingId);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    @Operation(summary = "Получить список всех тренировок.",
                    description = "Возвращает список тренировок.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренировки успешно получены.",
                                    content = @Content(mediaType = "application/json",
                                        array = @ArraySchema(
                                                schema = @Schema(implementation = ResponseTrainingDto.class) 
                                        ))
                            )
    })
    public ResponseEntity<?> findAllTrainigs() {
        List<ResponseTrainingDto> schedules = trainingService.findAllTrainigs();
        return ResponseEntity.ok().body(schedules);
    }

    @PostMapping("/{trainingId}/registration/{clientId}")
    @Operation(summary = "Записать клиента на тренировку.",
                    description = "Записывает клиента на выбранную тренировку.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Клиент успешно записан.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = String.class))
                            ),
                            @ApiResponse(
                                responseCode = "400",
                                description = "Неккоректные данные.",
                                content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Тренировка/Клиент с указанным идентификатором не найден.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> registrationClientForTraining(
            @Parameter(description = "Идентификатор тренировки", required = true)
            @PathVariable String trainingId,
            @Parameter(description = "Идентификатор клиента, которого необходимо записать", required = true)
            @PathVariable String clientId
    ) throws ResourceNotFoundException, InvalidDataException {
        trainingService.registrationClientForTraining(trainingId, clientId);
        return ResponseEntity.ok().body("Клиент записан");
    }

    @PutMapping("/{trainingId}")
    @Operation(summary = "Обновить тренировку по её идентификатору.",
                    description = "Обновляет тренировку по её идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренировка успешно обновлена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseTrainingDto.class))
                            ),
                            @ApiResponse(
                                responseCode = "400",
                                description = "Неккоректные данные.",
                                content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Тренировка с указанным id не найдена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> updateTraining(
            @Parameter(description = "Идентификатор тренировки, которую необходимо обновить.", required = true)
            @PathVariable String trainingId,
            @RequestBody @Parameter(description = "Сущность тренировки, для обновления существующей тренировки", required = true)
            CreateTrainingDto updateTrainingDto
    ) throws ResourceNotFoundException, InvalidDataException {
        ResponseTrainingDto updatedTraining = trainingService.updateTraining(trainingId, updateTrainingDto);
        return ResponseEntity.ok().body(updatedTraining);
    }

    @GetMapping("/{trainingId}")
    @Operation(summary = "Получить тренировку по её идентификатору.",
                    description = "Получить тренировку по её идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренировка успешно получена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseTrainingDto.class))
                            ),
                            @ApiResponse(
                                responseCode = "400",
                                description = "Неккоректные данные.",
                                content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Тренировка с указанным id не найдена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> getTraining(
            @Parameter(description = "Идентификатор тренировки, которую необходимо получить.", required = true)
            @PathVariable String trainingId
    ) throws ResourceNotFoundException {
        ResponseTrainingDto updatedTraining = trainingService.findTrainingById(trainingId);
        return ResponseEntity.ok().body(updatedTraining);
    }

    @GetMapping("/{trainingId}/registration")
    @Operation(summary = "Получить клиентов записанных на тренировку по её идентификатору.",
                description = "Возвращает список клиентов записанных на тренировку по её идентификатору.",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Список клиентов успешно получен.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ClientPojo.class))
                        ),
                        @ApiResponse(
                                responseCode = "404",
                                description = "Тренировка/Клиент с указанным идентификатором не найден.",
                                content = @Content(mediaType = "application/json",
                                        array = @ArraySchema(
                                                schema = @Schema(implementation = ResponseClientDto.class) 
                                        ))
                        )
    })
    public ResponseEntity<?> findTrainingClients(
            @Parameter(description = "Идентификатор тренировки", required = true)
            @PathVariable String trainingId
    ) throws ResourceNotFoundException {
        List<ClientPojo> clients = trainingService.findTrainingClients(trainingId);
        return ResponseEntity.ok().body(clients);
    }

}