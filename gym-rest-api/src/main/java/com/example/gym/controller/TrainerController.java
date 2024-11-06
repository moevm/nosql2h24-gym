package com.example.gym.controller;

import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.trainer.HourlyRateDto;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.trainer.UpdateTrainerDto;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingClientDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.service.TrainerService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainer", description = "Операции, связанные с тренерами")
public class TrainerController {

    private final TrainerService trainerService;
    private final ResponseService responseService;

    @GetMapping
    @Operation(summary = "Получить всех тренеров",
                    description = "Возвращает всех тренеров.",
                    responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Список тренеров успешно получен.",
                                content = @Content(mediaType = "application/json",
                                        array = @ArraySchema(
                                                schema = @Schema(implementation = ResponseTrainerWithoutTrainingsDto.class) 
                                        ))
                        )
    })
    public ResponseEntity<?> findAllTrainers(
            @Parameter(description = "Название секции для фильтрации", required = false)
            @RequestParam(name = "section", required = false) String section
    ) {
        List<ResponseTrainerWithoutTrainingsDto> trainers = trainerService.findAll(section);
        return ResponseEntity.ok().body(trainers);
    }

    @GetMapping("/free")
    @Operation(summary = "Получить всех тренеров, у которых есть свободные тренировки",
                    description = "Возвращает всех тренеров, у которых есть свободные тренировки.",
                    responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Список тренеров успешно получен.",
                                content = @Content(mediaType = "application/json",
                                        array = @ArraySchema(
                                                schema = @Schema(implementation = ResponseTrainerWithoutTrainingsDto.class) 
                                        ))
                        )
    })
    public ResponseEntity<?> findAllFreeTrainers(
            @Parameter(description = "Название секции для фильтрации", required = false)
            @RequestParam(name = "section", required = false) String section
    ) {
        System.out.println(section);
        List<ResponseTrainerWithoutTrainingsDto> trainers = trainerService.findAllFree(section);
        return ResponseEntity.ok().body(trainers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тренера по его индентификатору.",
                    description = "Возвращает тренера по его идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренер успешно получен.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseTrainerDto.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Тренер с указанным идентификатором не найден.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> findTrainerById(
            @Parameter(description = "Идентификатор тренера, которого нужно получить", required = true)
            @PathVariable String id
    ) {
        try {
            ResponseTrainerDto trainer = trainerService.findById(id);
            return ResponseEntity.ok().body(trainer);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity("Тренер с id %s не найден"
                    .formatted(id));
        }
    }

    @PutMapping("/{trainerId}")
    @Operation(summary = "Обновить тренера по его идентификатору.",
                    description = "Обновляет тренера по его идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренер успешно обновлен.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseTrainerDto.class))
                            ),
                            @ApiResponse(
                            responseCode = "400",
                            description = "Неккоректные данные.",
                            content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Тренер с указанным id не найден.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> updateTrainer(
            @PathVariable @Parameter(description = "Идентификатор тренера, которого необходимо обновить", required = true)
            String trainerId,
            @RequestBody @Parameter(description = "Сущность тренера, для обновления существуюшего тренера", required = true)
            UpdateTrainerDto dto
    ) {
        try {
            ResponseTrainerDto updatedTrainer = trainerService.updateTrainer(trainerId, dto);
            return ResponseEntity.ok().body(updatedTrainer);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        } catch (IllegalArgumentException exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        }
    }

    @DeleteMapping("/{trainerId}")
    @Operation(summary = "Удалить тренера по его идентификатору.",
                    description = "Удаляет тренера по его идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренер успешно удален.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseTrainerDto.class))
                            )
    })
    public ResponseEntity<?> deleteTrainer(
            @PathVariable @Parameter(description = "Идентификатор тренера, которого необходимо удалить", required = true)
            String trainerId
    ) {
        trainerService.deleteTrainer(trainerId);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/{trainerId}/set-hourly-rate")
    @Operation(summary = "Установить почасовую ставку тренера по его идентификатору.",
                    description = "Установить почасовую ставку тренера по его идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Почасовая ставка успешно установлена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseTrainerDto.class))
                            ),
                            @ApiResponse(
                            responseCode = "400",
                            description = "Неккоректные данные.",
                            content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Тренер с указанным id не найден.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> setHourlyRate(
            @PathVariable @Parameter(description = "Идентификатор тренера, которого необходимо обновить", required = true)
            String trainerId,
            @RequestBody @Parameter(description = "Сущность почасовой ставки, которую необходимо установить", required = true)
            HourlyRateDto dto
    ) {
        try {
            ResponseTrainerDto updatedTrainer = trainerService.setTrainerHourlyRate(trainerId, dto);
            return ResponseEntity.ok().body(updatedTrainer);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        }
    }

    @GetMapping("/{id}/trainings")
    @Operation(summary = "Получить тренировки по индентификатору тренера.",
                    description = "Возвращает список тренировок по идентификатору тренера.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренировки успешно получены.",
                                    content = @Content(mediaType = "application/json",
                                        array = @ArraySchema(
                                                schema = @Schema(implementation = ResponseTrainingDto.class) 
                                        ))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Тренер с указанным идентификатором не найден.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> findTrainerTrainings(
            @Parameter(description = "Идентификатор тренера, список тренировок которого нужно получить", required = true)
            @PathVariable String id
    ) {
        try {
            List<ResponseTrainingDto> schedules = trainerService.findTrainingsByTrainerId(id);
            return ResponseEntity.ok().body(schedules);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity("Тренер с id %s не найден"
                    .formatted(id));
        }
    }

    @GetMapping("/trainings")
    @Operation(summary = "Получить список всех тренировок.",
                    description = "Возвращает список тренировок тренировок.",
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
        List<ResponseTrainingDto> schedules = trainerService.findAllTrainigs();
        return ResponseEntity.ok().body(schedules);
    }

    @PostMapping("/{id}/trainings")
    @Operation(summary = "Добавить тренировку по идентификатору тренера.",
                    description = "Добавить тренировку по идентификатору тренера.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Тренировка успешно создана.",
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
                                    description = "Тренер с указанным идентификатором не найден.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> createTraining(
            @Parameter(description = "Идентификатор тренера, для которого необходимо добавить тренировку", required = true)
            @PathVariable String id,
            @RequestBody @Parameter(description = "Сущность тренировки, которую необходимо создать", required = true)
            CreateTrainingDto dto
    ) {
        try {
            ResponseTrainingDto createdTraining = trainerService.createTraining(dto, id);
            return ResponseEntity.created(URI.create("/training/" + createdTraining.getId())).body(createdTraining);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        } catch (IllegalArgumentException exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        }
    }

    @GetMapping("/{id}/trainings/{trainingId}")
    @Operation(summary = "Получить тренировку по её идентификатору и по идентификатору тренера.",
                    description = "Получить тренировку по её идентификатору и по идентификатору тренера.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренировка успешно получена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseTrainingClientDto.class))
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
            @Parameter(description = "Идентификатор тренера, для которого необходимо получить тренировку", required = true)
            @PathVariable String id,
            @Parameter(description = "Идентификатор тренировки, которую необходимо получить.", required = true)
            @PathVariable String trainingId
    ) {
        try {
            ResponseTrainingClientDto training = trainerService.findTrainingsByIdAndTrainerId(id, trainingId);
            return ResponseEntity.ok().body(training);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity("Некорректные данные");
        }
    }

    @PutMapping("/{id}/trainings/{trainingId}")
    @Operation(summary = "Обновить тренировку по её идентификатору и по идентификатору тренера.",
                    description = "Обновляет тренировку по её идентификатору и по идентификатору тренера.",
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
            @Parameter(description = "Идентификатор тренера, для которого необходимо обновить тренировку", required = true)
            @PathVariable String id,
            @Parameter(description = "Идентификатор тренировки, которую необходимо обновить.", required = true)
            @PathVariable String trainingId,
            @RequestBody @Parameter(description = "Сущность тренировки, для обновления существующей тренировки", required = true)
            CreateTrainingDto updateTrainingDto
    ) {
        try {
            ResponseTrainingDto updatedTraining = trainerService.updateTraining(id, trainingId, updateTrainingDto);
            return ResponseEntity.ok().body(updatedTraining);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity("Некорректные данные");
        }
    }

    @DeleteMapping("/{id}/trainings/{trainingId}")
    @Operation(summary = "Удалить тренировку по её идентификатору и по идентификатору тренера.",
                    description = "Удаляет тренировку по её идентификатору и по идентификатору тренера.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Тренировка успешно удалена."
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
    public ResponseEntity<?> deleteTrainingByTrainer(
            @Parameter(description = "Идентификатор тренера, для которого необходимо удалить тренировку", required = true)
            @PathVariable String id,
            @Parameter(description = "Идентификатор тренировки, которую необходимо удалить.", required = true)
            @PathVariable String trainingId
    ) {
        try {
            trainerService.deleteTrainingByTrainer(id, trainingId);
            return ResponseEntity.ok().body(null);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity("Некорректные данные");
        }
    }

    @PostMapping("/{id}/trainings/{trainingId}/registration/{clientId}")
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
                                    description = "Тренер/Тренировка/Клиент с указанным идентификатором не найден.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> registrationClientForTraining(
            @Parameter(description = "Идентификатор тренера", required = true)
            @PathVariable String id,
            @Parameter(description = "Идентификатор тренировки", required = true)
            @PathVariable String trainingId,
            @Parameter(description = "Идентификатор клиента, которого необходимо записать", required = true)
            @PathVariable String clientId
    ) {
        try {
            trainerService.registrationClientForTraining(id, trainingId, clientId);
            return ResponseEntity.ok().body("Клиент записан");
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        } catch (IllegalArgumentException exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        }
    }

    @GetMapping("/{id}/trainings/{trainingId}/registration")
    @Operation(summary = "Получить клиентов записанных на тренировку по её идентификатору.",
                description = "Возвращает список клиентов записанных на тренировку по её идентификатору.",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Список клиентов успешно получен.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseClientDto.class))
                        ),
                        @ApiResponse(
                                responseCode = "404",
                                description = "Тренер/Тренировка/Клиент с указанным идентификатором не найден.",
                                content = @Content(mediaType = "application/json",
                                        array = @ArraySchema(
                                                schema = @Schema(implementation = ResponseClientDto.class) 
                                        ))
                        )
    })
    public ResponseEntity<?> findTrainingClients(
            @Parameter(description = "Идентификатор тренера", required = true)
            @PathVariable String id,
            @Parameter(description = "Идентификатор тренировки", required = true)
            @PathVariable String trainingId
    ) {
        try {
            List<ResponseClientDto> clients = trainerService.findTrainingClients(id, trainingId);
            return ResponseEntity.ok().body(clients);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        }
    }

}
