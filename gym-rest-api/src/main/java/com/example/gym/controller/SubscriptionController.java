package com.example.gym.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.model.client.ResponseClientDto;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.subscription.dto.CreateSubscriptionDto;
import com.example.gym.model.subscription.dto.ResponseSubscriptionDto;
import com.example.gym.model.subscription.dto.UpdateSubscriptionDto;
import com.example.gym.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscription", description = "Операции связанные с абонементами.")
@Validated
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/client/{clientId}")
    @Operation(summary = "Создать абонемент по идентификатору клиента.",
            description = "Создает абонемент по идентификатору клиента.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Абонемент успешно создана.",
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
                            description = "Клиент с указанным идентификатором не найден.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> createSubscription(
            @RequestBody @Parameter(description = "Сущность абонемента, которого необходимо создать")
            @Valid CreateSubscriptionDto dto,
            @PathVariable @Parameter(description = "Идентификатор клиента, для которого создается абонемент.")
            String clientId
    ) throws ResourceNotFoundException {
        ResponseClientDto createdSubscription = subscriptionService.createSubscription(dto, clientId);
        return ResponseEntity.created(URI.create("/subscriptions/" + createdSubscription.getId())).body(createdSubscription);
    }

//     @GetMapping
//     @Operation(summary = "Получить все абонементы",
//                 description = "Возвращает все findAllSubscription.",
//                 responses = {
//                     @ApiResponse(
//                             responseCode = "200",
//                             description = "Список абонементов успешно получен.",
//                             content = @Content(mediaType = "application/json",
//                                     array = @ArraySchema(
//                                             schema = @Schema(implementation = ResponseSubscriptionDto.class) 
//                                     ))
//                     )
//     })
//     public ResponseEntity<?> findAllSubscriptions() {
//         List<ResponseSubscriptionDto> subscriptions = subscriptionService.findAllSubscription();
//         return ResponseEntity.ok().body(subscriptions);
//     }

//     @DeleteMapping("/client/{clientId}")
//     @Operation(summary = "Удаляет абонемент по идентификатору клиента.",
//             description = "Удаляет абонемент по идентификатору клиента.",
//             responses = {
//                     @ApiResponse(
//                             responseCode = "200",
//                             description = "Абонемент успешно удален."
//                     )
//     })
//     public ResponseEntity<?> deleteSubscriptionByClient(
//             @PathVariable @Parameter(description = "Идентификатор клиента, которому необходимо отменить абонемент")
//             String clientId
//     ) {
//         subscriptionService.deleteSubscriptionByClient(clientId);
//         return ResponseEntity.ok().body(null);
//     }

//     @DeleteMapping("/{subscriptionId}")
//     @Operation(summary = "Удалить абонемент по его идентификатору.",
//             description = "Удаляет абонемент по его идентификатору.",
//             responses = {
//                     @ApiResponse(
//                             responseCode = "200",
//                             description = "Абонемент успешно удален."
//                     )
//     })
//     public ResponseEntity<?> deleteSubscriptionById(
//             @PathVariable @Parameter(description = "Идентификатор абонемента, который необходимо удалить.")
//             String subscriptionId
//     ) {
//         subscriptionService.deleteSubscriptionById(subscriptionId);
//         return ResponseEntity.ok().body(null);
//     }

    // @GetMapping("/{clientId}")
    // @Operation(summary = "Получить абонемент по идентификатору клиента.",
    //                 description = "Возвращает абонемент по идентификатору клиента.",
    //                 responses = {
    //                         @ApiResponse(
    //                                 responseCode = "200",
    //                                 description = "Абонемент успешно получен.",
    //                                 content = @Content(mediaType = "application/json",
    //                                         schema = @Schema(implementation = ResponseSubscriptionDto.class))
    //                         ),
    //                         @ApiResponse(
    //                             responseCode = "400",
    //                             description = "Неккоректные данные.",
    //                             content = @Content(mediaType = "application/json",
    //                                         schema = @Schema(implementation = ResponseError.class))
    //                         ),
    //                         @ApiResponse(
    //                                 responseCode = "404",
    //                                 description = "Абонемент с указанным id не найден.",
    //                                 content = @Content(mediaType = "application/json",
    //                                         schema = @Schema(implementation = ResponseError.class))
    //                         )
    // })
    // private ResponseEntity<?> findSubscriptionById(
    //         @PathVariable @Parameter(description = "Идентификатор клиента абонимент, которого необходимо получить.")
    //         String clientId
    // ) {
    //     ResponseSubscriptionDto subscription = subscriptionService.findByClientId(clientId);
    //     return ResponseEntity.ok().body(subscription);
    // }

    @PutMapping("/{clientId}/extend")
    @Operation(summary = "Продлить абонемент по идентификатору клиента.",
            description = "Продливает идентификатор по идентификатору клиента.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Абонемент успешно продлен.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseSubscriptionDto.class))
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
    public ResponseEntity<?> extendSubscription(
            @RequestBody @Parameter(description = "Сущность для продления абонемента.")
            UpdateSubscriptionDto dto,
            @PathVariable @Parameter(description = "Идентификатор клиента абонемент, которого необходимо продлить.")
            String clientId
    ) {
        ResponseSubscriptionDto updatedSubscription = subscriptionService.extendSubscription(dto, clientId);
        return ResponseEntity.ok().body(updatedSubscription);
    }

    @PutMapping("/{clientId}/freeze")
    @Operation(summary = "Заморозить абонемент по идентификатору клиента.",
            description = "Замораживает абонемент по идентификатору клиента.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Абонемент успешно заморожен.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseSubscriptionDto.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Неккоректные данные.",
                        content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Абонемент с указанным id не найден.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
    })
    public ResponseEntity<?> freezeSubscription(
        @PathVariable @Parameter(description = "Идентификатор клиента абонемент, которого необходимо заморозить.")
        String clientId
    ) {
        ResponseSubscriptionDto subscription = subscriptionService.freezeSubscription(clientId);
        return ResponseEntity.ok().body(subscription);
    }

}
