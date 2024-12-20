package com.example.gym.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.exception.InvalidDataException;
import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.dto.statistics.trainer.ResponseTrainingForStatistics;
import com.example.gym.model.dto.statistics.trainer.TrainingDetailDto;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.trainer.ResponseTrainerWithoutTrainingsDto;
import com.example.gym.model.trainer.update.UpdateTrainerDto;
import com.example.gym.model.training.dto.CreateTrainingDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.service.TrainerService;

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
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainer", description = "Операции, связанные с тренерами")
@Validated
public class TrainerController {

    private final TrainerService trainerService;

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
            @RequestParam(name = "section", required = false) String section,
            @Parameter(description = "Имя тренера", required = false)
            @RequestParam(name = "name", required = false) String name,
            @Parameter(description = "Фамилия тренера", required = false)
            @RequestParam(name = "surname", required = false) String surname,
            @Parameter(description = "Пол тренера", required = false, example = "MALE")
            @RequestParam(name = "gender", required = false) Optional<GenderType> gender,
            @Parameter(description = "День рожения от", required = false)
            @RequestParam(name = "birthdayFrom", required = false) LocalDateTime birthdayFrom,
            @Parameter(description = "День рожения до", required = false)
            @RequestParam(name = "birthdayBefore", required = false) LocalDateTime birthdayBefore
    ) {
        List<ResponseTrainerWithoutTrainingsDto> trainers = trainerService.findAll(section, name, surname, gender, birthdayFrom, birthdayBefore);
        return ResponseEntity.ok().body(trainers);
    }

//     @GetMapping("/free")
//     @Operation(summary = "Получить всех тренеров, у которых есть свободные тренировки",
//                     description = "Возвращает всех тренеров, у которых есть свободные тренировки.",
//                     responses = {
//                         @ApiResponse(
//                                 responseCode = "200",
//                                 description = "Список тренеров успешно получен.",
//                                 content = @Content(mediaType = "application/json",
//                                         array = @ArraySchema(
//                                                 schema = @Schema(implementation = ResponseTrainerWithoutTrainingsDto.class) 
//                                         ))
//                         )
//     })
//     public ResponseEntity<?> findAllFreeTrainers(
//             @Parameter(description = "Название секции для фильтрации", required = false)
//             @RequestParam(name = "section", required = false) String section
//     ) {
//         System.out.println(section);
//         List<ResponseTrainerWithoutTrainingsDto> trainers = trainerService.findAllFree(section);
//         return ResponseEntity.ok().body(trainers);
//     }

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
    ) throws ResourceNotFoundException {
        ResponseTrainerDto trainer = trainerService.findById(id);
        return ResponseEntity.ok().body(trainer);
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
            @Valid UpdateTrainerDto dto
    ) throws UniquenessViolationException, ResourceNotFoundException {
        ResponseTrainerDto updatedTrainer = trainerService.updateTrainer(trainerId, dto);
        return ResponseEntity.ok().body(updatedTrainer);
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
        List<ResponseTrainingDto> schedules = trainerService.findTrainigs(id);
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
    ) throws ResourceNotFoundException, InvalidDataException {
        ResponseTrainingDto createdTraining = trainerService.createTraining(dto, id);
        return ResponseEntity.created(URI.create("/training/" + createdTraining.getId())).body(createdTraining);
    }

    @GetMapping("/{id}/statistics/trainings")
    @Operation(summary = "Получить статистику по тренировкам.",
            description = "Возвращает статистику по тренировкам.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена.",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = TrainingDetailDto.class)
                                    ))
                    )
            })
    public ResponseEntity<Page<TrainingDetailDto>> getTrainingsStatistics(
            @Parameter(description = "Идентификатор тренера", required = true)
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page, // Номер страницы
            @RequestParam(defaultValue = "10") int size, // Размер страницы
            @RequestParam(required = false) String dateRangeFrom,
            @RequestParam(required = false) String dateRangeTo,
            @RequestParam(required = false) Double aboveProfit,
            @RequestParam(required = false) Integer aboveClients

            // @ApiParam(value = "Начальная дата", required = true)
            // @RequestParam LocalDate startDate,
            // @ApiParam(value = "Конечная дата", required = true)
            // @RequestParam LocalDate endDate
    ) {
        LocalDateTime from = dateRangeFrom != null
                ? LocalDateTime.parse(dateRangeFrom.replace("Z", ""))
                : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime to = dateRangeTo != null
                ? LocalDateTime.parse(dateRangeTo.replace("Z", ""))
                : LocalDateTime.of(9999, 12, 31, 23, 59);

        Page<TrainingDetailDto> statistics = trainerService.getTrainingsStatistics(
                id, page, size, from, to, aboveProfit, aboveClients);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/{id}/statistics/profit")
    @Operation(summary = "Получить статистику по прибыли.",
            description = "Возвращает статистику по прибыли.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class))
                    )
            })
    public ResponseEntity<Map<String, Object>> getProfitStatistics(
            @Parameter(description = "Идентификатор тренера", required = true)
            @PathVariable String id
    ) {
        Map<String, Object> profitStatistics = trainerService.getProfitStatistics(id);
        return ResponseEntity.ok().body(profitStatistics);
    }

    @GetMapping("/{id}/statistics/trainings/{trainingId}")
    @Operation(summary = "Получить статистику тренировок по промежутку времени.",
            description = "Возвращает статистику тренировок по промежутку времени.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseTrainingForStatistics.class))
                    )
            })
    public ResponseEntity<?> getTrainingStatistics(
            @Parameter(description = "Идентификатор тренера", required = true)
            @PathVariable String id,
            @Parameter(description = "Идентификатор тренировки", required = true)
            @PathVariable String trainingId
    ) {
        return ResponseEntity.ok().body(trainerService.getTrainingStatistics(id, trainingId));
    }

}
