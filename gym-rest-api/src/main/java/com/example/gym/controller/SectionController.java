package com.example.gym.controller;

import java.net.URI;
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

import com.example.gym.model.dto.ResponseError;
import com.example.gym.model.section.Section;
import com.example.gym.model.section.dto.CreateSectionDto;
import com.example.gym.model.section.dto.ResponseSectionDto;
import com.example.gym.model.trainer.ResponseTrainerDto;
import com.example.gym.model.training.dto.ResponseTrainingDto;
import com.example.gym.service.SectionService;
import com.example.gym.service.TrainerService;
import com.example.gym.util.ResponseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
@Tag(name = "Section", description = "Операции, связанные с секциями")
public class SectionController {

    private final SectionService sectionService;
    private final ResponseService responseService;
    private final TrainerService trainerService;

    @PostMapping
    @Operation(summary = "Создать секцию.",
                description = "Создает секцию.",
                responses = {
                        @ApiResponse(
                                responseCode = "201",
                                description = "Секция успешно создана.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseSectionDto.class))
                        ),
                        @ApiResponse(
                            responseCode = "400",
                            description = "Неккоректные данные.",
                            content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseError.class))
                        )
    })
    public ResponseEntity<?> createSection(
            @RequestBody @Parameter(description = "Сущность секции, которую необходимо создать", required = true)
            CreateSectionDto dto
    ) {
        try {
            ResponseSectionDto createdSection = sectionService.createSection(dto);
            return ResponseEntity.created(URI.create("/sections/" + createdSection.getId())).body(createdSection);
        } catch (IllegalArgumentException exception) {
            return responseService.getBadRequestResponseEntity("Секция с таким названием уже существует");
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity("Некорректные данные");
        }
    }

    @GetMapping
    @Operation(summary = "Получить все секции",
                description = "Возвращает все секции.",
                responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список секций успешно получен.",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ResponseSectionDto.class) 
                                    ))
                    )
    })
    public ResponseEntity<?> findAllSections() {
        List<ResponseSectionDto> sections = sectionService.findAllSections();
        return ResponseEntity.ok().body(sections);
    }

    @GetMapping("/{sectionId}")
    @Operation(summary = "Получить секцию по ее индентификатору.",
                description = "Возвращает секцию по ее идентификатору.",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Секция успешно получена.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseSectionDto.class))
                        ),
                        @ApiResponse(
                                responseCode = "404",
                                description = "Секция с указанным идентификатором не найдена.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseError.class))
                        )
    })
    public ResponseEntity<?> findSectionById(
            @PathVariable @Parameter(description = "Идентификатор секции, которую необходимо получить.")
            String sectionId
    ) {
        try {
            ResponseSectionDto section = sectionService.findSectionById(sectionId);
            return ResponseEntity.ok().body(section);
        } catch (IllegalArgumentException exception) {
            return responseService.getNotFoundResponseEntity("Секция с id %s не найдена"
                    .formatted(sectionId));
        }
    }

    @PutMapping("/{sectionId}")
    @Operation(summary = "Обновить секцию по её идентификатору.",
                description = "Обновляет секцию по её идентификатору.",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Секция успешно обновлена.",
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
                                description = "Секция с указанным id не найдена.",
                                content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ResponseError.class))
                        )
    })
    public ResponseEntity<?> updateSection(
            @PathVariable @Parameter(description = "Идентификатор секции, которую необходиме обновить.")
            String sectionId,
            @RequestBody @Parameter(description = "Сущность секции, для обновления существующей секции")
            CreateSectionDto dto
    ) {
        try {
            ResponseSectionDto updatedSection = sectionService.updateSection(sectionId, dto);
            return ResponseEntity.ok().body(updatedSection);
        } catch (IllegalArgumentException exception) {
            return responseService.getNotFoundResponseEntity("Секция с id %s не найдена"
                    .formatted(sectionId));
        } catch (Exception exception) {
            return responseService.getBadRequestResponseEntity("Некорректные данные");
        }
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<?> deleteSection(
            @PathVariable @Parameter(description = "Идентификатор секции, которую необходимо удалить.")
            String sectionId
    ) {
        sectionService.deleteSectionById(sectionId);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/{sectionId}/trainers")
    public ResponseEntity<?> findSectionTrainers(
            @PathVariable @Parameter(description = "Идентификатор секции, для которой необходимо получить тренеров")
            String sectionId
    ) {
        try {
            Section section = sectionService.getById(sectionId);
            List<ResponseTrainerDto> trainers = trainerService.findTrainersBySection(section);
            return ResponseEntity.ok().body(trainers);
        } catch (IllegalArgumentException exception) {
            return responseService.getNotFoundResponseEntity("Секция с id %s не найдена"
                    .formatted(sectionId));
        }
    }
}
