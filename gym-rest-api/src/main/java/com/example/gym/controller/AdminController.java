package com.example.gym.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.admin.UpdateAdminDto;
import com.example.gym.model.client.ResponseClientForStatistic;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.trainer.ResponseTrainerForStatistic;
import com.example.gym.model.training.dto.ResponseTrainingForStatistic;
import com.example.gym.service.AdminService;
import com.example.gym.service.ClientService;
import com.example.gym.service.TrainerService;
import com.example.gym.service.TrainingService;
import com.example.gym.util.ResponseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ResponseService responseService;
    private final TrainingService trainingService;
    private final ClientService clientService; 
    private final TrainerService trainerService;

    @GetMapping
    @Operation(summary = "Получить всех админов",
                description = "Возвращает всех админов.",
                responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список админов успешно получен.",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ResponseAdminDto.class) 
                                    ))
                    )
    })
    public ResponseEntity<?> findAllAdmins() {
        List<ResponseAdminDto> admins = adminService.findAllAdmins();
        return ResponseEntity.ok().body(admins);
    }

    @GetMapping("/{adminId}")
    @Operation(summary = "Получить админа по его индентификатору.",
                description = "Возвращает админа по его идентификатору.",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Админ успешно получен.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseAdminDto.class))
                        ),
                        @ApiResponse(
                                responseCode = "404",
                                description = "Админ с указанным идентификатором не найден.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseError.class))
                        )
    })
    public ResponseEntity<?> getAdminById(
            @PathVariable @Parameter(description = "Идентификатор админа, которого необходимо получить", required = true)
            String adminId
    ) {
        try {
            ResponseAdminDto adminDto = adminService.findAdminById(adminId);
            return ResponseEntity.ok().body(adminDto);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        }
    }

    @DeleteMapping("/{adminId}")
    @Operation(summary = "Удалить админа по его индентификатору.",
                description = "Удаляет админа по его идентификатору.",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Админ успешно удален.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseAdminDto.class))
                        )
    })
    public ResponseEntity<?> deleteAdmin(
            @PathVariable @Parameter(description = "Идентификатор админа, которого необходимо удалить", required = true)
            String adminId
    ) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/{adminId}")
    @Operation(summary = "Обновить админа по его идентификатору.",
                    description = "Обновляет админа по его идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Админ успешно обновлен.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseAdminDto.class))
                            ),
                            @ApiResponse(
                                responseCode = "400",
                                description = "Неккоректные данные.",
                                content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Админ с указанным id не найден.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> updateAdmin(
            @PathVariable @Parameter(description = "Идентификатор админа, которого необходимо обновить", required = true)
            String adminId,
            @RequestBody @Parameter(description = "Сущность админа, для обновления существуюшего админа", required = true)
            UpdateAdminDto dto
    ) {
        try {
            ResponseAdminDto updatedAdmin = adminService.updateAdmin(adminId, dto);
            return ResponseEntity.ok().body(updatedAdmin);
        } catch (NoResultException exception) {
            return responseService.getNotFoundResponseEntity(exception.getMessage());
        } catch (IllegalArgumentException exception) {
            return responseService.getBadRequestResponseEntity(exception.getMessage());
        }
    }

//     @GetMapping("/trainings-conducted")
//     @Operation(summary = "Получить все проведенные тренировки.",
//         description = "Возвращает список всех проведенных тренировок.",
//         responses = {
//                 @ApiResponse(
//                         responseCode = "200",
//                         description = "Список проведенных тренировок успешно получен.",
//                         content = @Content(mediaType = "application/json",
//                                 schema = @Schema(implementation = ResponseTrainingForStatistic.class))
//                 )
//     })
//     public ResponseEntity<?> getNumberOfTrainingsConducted() {
//         ResponseTrainingForStatistic conductedTrainings = trainingService.findAllConductedTrainings();
//         return ResponseEntity.ok().body(conductedTrainings);
//     }

//     @GetMapping("/clients-activity")
//     @Operation(summary = "Получить активность всех клиентов.",
//                 description = "Возвращает список всех активности всех клиентов.",
//                 responses = {
//                         @ApiResponse(
//                                 responseCode = "200",
//                                 description = "Активность клиентов успешно получена.",
//                                         content = @Content(mediaType = "application/json",
//                                                 array = @ArraySchema(
//                                                         schema = @Schema(implementation = ResponseClientForStatistic.class) 
//                                                 ))
//                         )
//     })
//     public ResponseEntity<?> getClientsActivity() {
//         List<ResponseClientForStatistic> clientForStatistic = clientService.getClientsActivity();
//         return ResponseEntity.ok().body(clientForStatistic);
//     }

//     @GetMapping("/trainers-activity")
//     @Operation(summary = "Получить активность всех тренеров.",
//                 description = "Возвращает список всех активности всех тренеров.",
//                 responses = {
//                         @ApiResponse(
//                                 responseCode = "200",
//                                 description = "Активность тренеров успешно получена.",
//                                         content = @Content(mediaType = "application/json",
//                                                 array = @ArraySchema(
//                                                         schema = @Schema(implementation = ResponseTrainerForStatistic.class) 
//                                                 ))
//                         )
//     })
//     public ResponseEntity<?> getTrainersActivity() {
//         List<ResponseTrainerForStatistic> trainersForStatistic = trainerService.getTrainersActivity();
//         return ResponseEntity.ok().body(trainersForStatistic);
//     }


} 
