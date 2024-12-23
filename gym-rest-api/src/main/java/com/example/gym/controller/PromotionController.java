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

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.promotion.dto.CreatePromotionDto;
import com.example.gym.model.promotion.dto.ResponsePromotionDto;
import com.example.gym.model.promotion.dto.UpdatePromotionDto;
import com.example.gym.service.PromotionService;

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
@RequestMapping("/promotions")
@RequiredArgsConstructor
@Tag(name = "Promotion", description = "Операции, связанные с promotion")
@Validated
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping("")
    @Operation(summary = "Создать промо акцию.",
            description = "Создать промо акцию.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Промо акция успешно создана.",
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
            @RequestBody @Parameter(description = "Сущность промо акции, которую необходимо создать", required = true)
            @Valid CreatePromotionDto dto
    ) throws ResourceNotFoundException {
        ResponsePromotionDto createdPromotion = promotionService.create(dto);
        return ResponseEntity.created(URI.create("/promotions/" + createdPromotion.getId())).body(createdPromotion);
    }

    @GetMapping
    @Operation(summary = "Получить все промо акции",
            description = "Возвращает все промо акции.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список промо акций успешно получен.",
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
    @Operation(summary = "Получить промо акцию по ее идентификатору.",
            description = "Получить промо акцию по ее идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Промо акция успешно получена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponsePromotionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Промо акция с указанным id не найдена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
            })

    public ResponseEntity<?> findPromotionById(
            @PathVariable @Parameter(description = "Идентификатор промо акции, котороую необходимо получить", required = true)
            String id
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(promotionService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить промо акцию по её идентификатору.",
            description = "Обновляет промо акцию по её идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Промо акция успешно обновлена.",
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
                            description = "Промо акция с указанным id не найдена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class))
                    )
            })
    public ResponseEntity<?> editPromotion(
            @PathVariable @Parameter(description = "Идентификатор промо акции, которую необходимо обновить", required = true)
            String id,
            @RequestBody @Parameter(description = "Сущность промо акции, для обновления", required = true)
            @Valid UpdatePromotionDto dto
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(promotionService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить промо акцию по ее идентификатору.",
            description = "Удаляет промо акцию по е идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Промо акция успешно удалена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    )
            })
    public ResponseEntity<?> deletePromotion(
            @PathVariable @Parameter(description = "Идентификатор промо акции, которую необходимо удалить", required = true)
            String id
    ) throws ResourceNotFoundException {
        promotionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

}
