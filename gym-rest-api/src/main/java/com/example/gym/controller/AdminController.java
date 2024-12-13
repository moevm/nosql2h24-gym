package com.example.gym.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.admin.UpdateAdminDto;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.dto.statistics.admin.ApplicationStatistics;
import com.example.gym.model.dto.statistics.admin.PurchasedSubcriptions;
import com.example.gym.model.dto.statistics.admin.SectionStatistics;
import com.example.gym.model.settings.dto.CreateLoyaltySettingsDto;
import com.example.gym.model.subscription.SubscriptionStatus;
import com.example.gym.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;

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
    public ResponseEntity<?> findAllAdmins(
            @Parameter(description = "Имя администратора", required = false)
            @RequestParam(name = "name", required = false) String name,
            @Parameter(description = "Фамилия администратора", required = false)
            @RequestParam(name = "surname", required = false) String surname,
            @Parameter(description = "День рожения от", required = false)
            @RequestParam(name = "birthdayFrom", required = false) LocalDateTime birthdayFrom,
            @Parameter(description = "День рожения до", required = false)
            @RequestParam(name = "birthdayBefore", required = false) LocalDateTime birthdayBefore
    ) {
        List<ResponseAdminDto> admins = adminService.findAllAdmins(name, surname, birthdayFrom, birthdayBefore);
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
    ) throws ResourceNotFoundException {
        ResponseAdminDto adminDto = adminService.findAdminById(adminId);
        return ResponseEntity.ok().body(adminDto);
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
            @Valid UpdateAdminDto dto,
            BindingResult bindingResult
    ) throws ResourceNotFoundException, UniquenessViolationException {
        ResponseAdminDto updatedAdmin = adminService.updateAdmin(adminId, dto);
        return ResponseEntity.ok().body(updatedAdmin);
    }

    @GetMapping("/users")
    @Operation(summary = "Получить всех пользователей.",
            description = "Возвращает всех пользователей по его идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователи успешно получены.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseAdminDto.class))
                    )
    })
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(adminService.findAllUsers());
    }

    @PostMapping("/settings/loyalty")
    public ResponseEntity<?> setAcceptanceRate(
            @RequestBody @Parameter(description = "Сущность для настройки зачисления бонусов", required = true)
            @Valid CreateLoyaltySettingsDto dto
    ) {
        adminService.setAcceptanceRate(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics/application")
    @Operation(summary = "Получить статистику приложения по промежутку времени.",
            description = "Возвращает статистику приложения по промежутку времени.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApplicationStatistics.class))
                    )
    })
    public ResponseEntity<?> getApplicationStatistics(
        // @RequestParam @Parameter(description = "Начальная дата", required = true) 
        // LocalDate startDate,
        // @RequestParam @Parameter(description = "Конечная дата", required = true) 
        // LocalDate endDate
    ) {
        return ResponseEntity.ok().body(adminService.getApplicationStatistics());
    }

    @GetMapping("/statistics/subscriptions")
    @Operation(summary = "Получить статистику абонементов по промежутку времени.",
            description = "Возвращает статистику абонементов по промежутку времени.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PurchasedSubcriptions.class))
                    )
    })
    public ResponseEntity<?> getSubscriptionsStatistics(
        @RequestParam(required = false) @Parameter(description = "Id клиента", required = false) 
        String clientId,
        @RequestParam(required = false) @Parameter(description = "Начальная дата", required = false) 
        LocalDate startDate,
        @RequestParam(required = false) @Parameter(description = "Конечная дата", required = false) 
        LocalDate endDate,
        @RequestParam(required = false) @Parameter(description = "Статус", required = false) 
        SubscriptionStatus status,
        @RequestParam(required = false, defaultValue = "0") @Parameter(description = "Номер страницы", required = false) int page,
        @RequestParam(required = false, defaultValue = "5") @Parameter(description = "Размер страницы", required = false) int size
    ) {
        return ResponseEntity.ok().body(adminService.getPurchasedSubscriptions(clientId, startDate, endDate, status, page, size));
    }

    @GetMapping("/statistics/trainings")
    @Operation(summary = "Получить статистику тренировок по промежутку времени.",
            description = "Возвращает статистику тренировок по промежутку времени.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена.",
                            content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                schema = @Schema(implementation = SectionStatistics.class)
                            ))
                    )
    })
    public ResponseEntity<?> getTrainingsStatistics(
        @RequestParam @Parameter(description = "Начальная дата", required = true) 
        LocalDate startDate,
        @RequestParam @Parameter(description = "Конечная дата", required = true) 
        LocalDate endDate,
        @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы", required = false) int page,
        @RequestParam(required = false, defaultValue = "5") @Parameter(description = "Размер страницы", required = false) int size
    ) {
        return ResponseEntity.ok().body(adminService.getFinishedTrainings(startDate, endDate, page, size));
    }

    // @GetMapping("/statistics/trainers")
    // @Operation(summary = "Получить статистику по тренерам по промежутку времени.",
    //         description = "Возвращает статистику по тренерам по промежутку времени.",
    //         responses = {
    //                 @ApiResponse(
    //                         responseCode = "200",
    //                         description = "Статистика успешно получена.",
    //                         content = @Content(mediaType = "application/json",
    //                                 array = @ArraySchema(
    //                                     schema = @Schema(implementation = TrainerStatistics.class)
    //                                 ))
    //                 )
    // })
    // public ResponseEntity<?> getTrainersStatistics(
    //     @RequestParam @Parameter(description = "Начальная дата", required = true) 
    //     LocalDate startDate,
    //     @RequestParam @Parameter(description = "Конечная дата", required = true) 
    //     LocalDate endDate
    // ) {
    //     return ResponseEntity.ok().body(adminService.getTrainersStatistics(startDate, endDate));
    // }

    
    @GetMapping("/statistics/room")
    @Operation(summary = "Получить статистику по залам по промежутку времени.",
            description = "Возвращает статистику по залам по промежутку времени.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PurchasedSubcriptions.class))
                    )
    })
    public ResponseEntity<?> getRoomStatistics(
        @RequestParam @Parameter(description = "Начальная дата", required = true) 
        LocalDate startDate,
        @RequestParam @Parameter(description = "Конечная дата", required = true) 
        LocalDate endDate
    ) {
        return ResponseEntity.ok().body(adminService.getRoomsActive(startDate, endDate));
    }

    @GetMapping("/statistics/finance")
    @Operation(summary = "Получить финансувую статистику.",
            description = "Возвращает финансувую статистику.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PurchasedSubcriptions.class))
                    )
    })
    public ResponseEntity<?> getFinanceStatistics(
        @RequestParam @Parameter(description = "Начальная дата", required = true) 
        LocalDate startDate,
        @RequestParam @Parameter(description = "Конечная дата", required = true) 
        LocalDate endDate
    ) {
        return ResponseEntity.ok().body(adminService.getFinanceActivity(startDate, endDate));
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
