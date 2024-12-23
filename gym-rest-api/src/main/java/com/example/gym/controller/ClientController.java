package com.example.gym.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.client.UpdateClientDto;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.training.dto.ResponseTrainingForClientDto;
import com.example.gym.model.user.pojo.GenderType;
import com.example.gym.service.ClientService;

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
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Операции связанные с клиентами.")
@Validated
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @Operation(summary = "Получить всех клиентов",
            description = "Возвращает всех клиентов.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Список клиентов успешно получен.",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(
                                        schema = @Schema(implementation = ResponseClientDto.class) 
                                ))
                )
    })
    public ResponseEntity<?> findAllClients(
            @Parameter(description = "Имя клиента", required = false)
            @RequestParam(name = "name", required = false) String name,
            @Parameter(description = "Фамилия клиента", required = false)
            @RequestParam(name = "surname", required = false) String surname,
            @Parameter(description = "Пол клиента", required = false, example = "'MALE' или 'FEMALE'")
            @RequestParam(name = "gender", required = false) Optional<GenderType> gender,
            @Parameter(description = "День рожения от", required = false)
            @RequestParam(name = "birthdayFrom", required = false) LocalDateTime birthdayFrom,
            @Parameter(description = "День рожения до", required = false)
            @RequestParam(name = "birthdayBefore", required = false) LocalDateTime birthdayBefore
    ) {
        List<ResponseClientDto> clients = clientService.findAllClients(name, surname, gender, birthdayFrom, birthdayBefore);
        return ResponseEntity.ok().body(clients);
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Получить клиента по его индентификатору.",
            description = "Возвращает клиента по его идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Клиент успешно получен.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseClientDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Клиент с указанным идентификатором не найден.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> findClientById(
            @PathVariable @Parameter(description = "Идентификатор клиента, которго необходимо получить", required = true)
            String clientId
    ) throws ResourceNotFoundException {
        ResponseClientDto client = clientService.findClientById(clientId);
        return ResponseEntity.ok().body(client);
    }

    @PutMapping("/{clientId}")
    @Operation(summary = "Обновить клиента по его идентификатору.",
            description = "Обновляет клиента по его идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Клиент успешно обновлен.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseClientDto.class))
                    ),
                    @ApiResponse(
                    responseCode = "400",
                    description = "Неккоректные данные.",
                    content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Клиент с указанным id не найден.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> updateClient(
            @PathVariable @Parameter(description = "Идентификатор клиента, которого необходимо обновить", required = true)
            String clientId,
            @RequestBody @Parameter(description = "Сущность клиента, для обновления существуюшего клиента", required = true)
            @Valid UpdateClientDto dto
    ) throws ResourceNotFoundException, UniquenessViolationException {
        ResponseClientDto updatedClient = clientService.updateClient(clientId, dto);
        return ResponseEntity.ok().body(updatedClient);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(
            @PathVariable @Parameter(description = "Идентификатор клиента, которого необходимо обновить", required = true)
            String clientId
    ) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/{clientId}/trainings")
    @Operation(summary = "Получить тренировки клиента по его индентификатору.",
            description = "Возвращает список тренировок клиента по его идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список тренировок успешно получен.",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ResponseTrainingForClientDto.class) 
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Клиент с указанным идентификатором не найден.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> findClientTrainings(
            @PathVariable @Parameter(description = "Идентификатор клиента, которого необходимо получить", required = true)
            String clientId
    ) {
        List<ResponseTrainingForClientDto> trainings = clientService.findClientTrainings(clientId);
        return ResponseEntity.ok().body(trainings);
    }

    @GetMapping("/{clientId}/subscription")
    @Operation(summary = "Получить абонемент клиента по его индентификатору.",
            description = "Возвращает абонемент клиента по его идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Абонемент успешно получен.",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ResponseSubscriptionDto.class) 
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Клиент с указанным идентификатором не найден.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> findClientSubscription(
            @PathVariable @Parameter(description = "Идентификатор клиента, абонемент которго необходимо получить", required = true)
            String clientId
    ) {
        ResponseSubscriptionDto subcription = clientService.findClientSubscription(clientId);
        return ResponseEntity.ok().body(subcription);
    }

}
