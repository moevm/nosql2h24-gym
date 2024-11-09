package com.example.gym.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.model.promotion.CreatedBy;
import com.example.gym.model.promotion.Promotion;
import com.example.gym.model.promotion.dto.CreatePromotionDto;
import com.example.gym.model.promotion.dto.ResponsePromotionDto;
import com.example.gym.model.user.User;
import com.example.gym.repository.PromotionRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final Mapper modelMapper;

    public ResponsePromotionDto create(CreatePromotionDto dto) throws ResourceNotFoundException {
        Promotion promotion = modelMapper.toModel(dto);

        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID %s не найден".formatted(dto.getCreatorId())));

        CreatedBy createdBy = new CreatedBy(creator.getId(), creator.getName(), creator.getSurname());
        promotion.setCreatedBy(createdBy);

        return modelMapper.toDto(promotionRepository.save(promotion));
    }

    public List<ResponsePromotionDto> findAll() {
        return promotionRepository.findAll().stream()
                .map(modelMapper::toDto)
                .toList();
    }

    public ResponsePromotionDto findById(String id) throws ResourceNotFoundException {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Поощрения с ID %s не найдено".formatted(id)));

        return modelMapper.toDto(promotion);
    }

    public ResponsePromotionDto update(CreatePromotionDto dto, String id) throws ResourceNotFoundException {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Поощрения с ID %s не найдено".formatted(id)));

        promotion.setName((dto.getName() != null ? dto.getName() : promotion.getName()));
        promotion.setDescription((dto.getDescription() != null ? dto.getDescription() : promotion.getDescription()));
        promotion.setStartDate((dto.getStartDate() != null ? dto.getStartDate() : promotion.getStartDate()));
        promotion.setEndDate((dto.getEndDate() != null ? dto.getEndDate() : promotion.getEndDate()));
        promotion.setDiscountPercentage(
                (dto.getDiscountPersentage() != null ? dto.getDiscountPersentage() : promotion.getDiscountPercentage()));
        
        if (dto.getCreatorId() != null) {
            User creator = userRepository.findById(dto.getCreatorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID %s не найден".formatted(id)));
            
            promotion.setCreatedBy(new CreatedBy(creator.getId(), creator.getName(), creator.getSurname()));
        }

        return modelMapper.toDto(promotionRepository.save(promotion));
    }

    public void delete(String id) {
        promotionRepository.deleteById(id);
    }

}
