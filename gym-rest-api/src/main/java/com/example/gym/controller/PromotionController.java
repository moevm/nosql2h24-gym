package com.example.gym.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.promotion.dto.CreatePromotionDto;
import com.example.gym.model.promotion.dto.ResponsePromotionDto;
import com.example.gym.service.PromotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
@Tag(name = "Promotion", description = "Операции, связанные с promotion")
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping("")
    @Operation(summary = "Создать поощрение.",
                    description = "Создать поощрение.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Поощрение успешно создано.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponsePromotionDto.class))
                            ),
                            @ApiResponse(
                                responseCode = "400",
                                description = "Неккоректные данные.",
                                content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            ),
                            @ApiResponse(
                                responseCode = "404",
                                description = "Пользовать с указанным ID не найден.",
                                content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> createPromotion(
            @RequestBody @Parameter(description = "Сущность поощрения, которое необходимо создать", required = true)
            CreatePromotionDto dto
    ) throws ResourceNotFoundException {
        ResponsePromotionDto createdPromotion = promotionService.create(dto);
        return ResponseEntity.created(URI.create("/promotions/" + createdPromotion.getId())).body(createdPromotion);
    }

    @GetMapping
    @Operation(summary = "Получить все поощрения",
                    description = "Возвращает все поощрения.",
                    responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Список поощрений успешно получен.",
                                content = @Content(mediaType = "application/json",
                                        array = @ArraySchema(
                                                schema = @Schema(implementation = ResponsePromotionDto.class) 
                                        ))
                        )
    })
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(promotionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить поощрение по его идентификатору.",
                    description = "Получить поощрение по его идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Поощрение успешно получено.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponsePromotionDto.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Поощрение с указанным id не найдено.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })

    public ResponseEntity<?> findPromotionById(
            @PathVariable @Parameter(description = "Идентификатор поощрения, которое необходимо получить", required = true)
            String id
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(promotionService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить поощрение по её идентификатору.",
                    description = "Обновляет поощрение по её идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Поощрение успешно обновлена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponsePromotionDto.class))
                            ),
                            @ApiResponse(
                                responseCode = "400",
                                description = "Неккоректные данные.",
                                content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Поощрение с указанным id не найдена.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ResponseError.class))
                            )
    })
    public ResponseEntity<?> editPromotion(
            @PathVariable @Parameter(description = "Идентификатор поощрения, которое необходимо обновить", required = true)
            String id,
            @RequestBody @Parameter(description = "Сущность поощрения, для обновления", required = true)
            CreatePromotionDto dto
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(promotionService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить поощрение по его идентификатору.",
                    description = "Удаляет поощрение по его идентификатору.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Поощрение успешно удалено.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = String.class))
                            )
    })
    public ResponseEntity<?> deletePromotion(
            @PathVariable @Parameter(description = "Идентификатор поощрения, которое необходимо удалить", required = true)
            String id
    ) throws ResourceNotFoundException {
        promotionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

}
