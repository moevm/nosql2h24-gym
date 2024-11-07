package com.example.gym.service;

import com.example.gym.model.section.Section;
import com.example.gym.model.section.dto.CreateSectionDto;
import com.example.gym.model.section.dto.ResponseSectionDto;
import com.example.gym.repository.SectionRepository;
import com.example.gym.util.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final Mapper modelMapper;

    @Transactional
    public ResponseSectionDto createSection(CreateSectionDto dto) {
        if (!sectionRepository.findByName(dto.getName()).isEmpty()) {
            throw new IllegalArgumentException();
        }

        Section section = modelMapper.toModel(dto);
        Section createdSection = sectionRepository.save(section);
        return modelMapper.toSectionDto(createdSection);
    }

    public List<ResponseSectionDto> findAllSections() {
        return sectionRepository.findAll().stream()
                .map(s -> modelMapper.toSectionDto(s))
                .toList();
    }

    public ResponseSectionDto findSectionById(String id) {
        Section section = getById(id);
        return modelMapper.toSectionDto(section);
    }

    @Transactional
    public ResponseSectionDto updateSection(String id, CreateSectionDto dto) {
        Section section = getById(id);
        section.setName(dto.getName());
        Section updatedSection = sectionRepository.save(section);
        return modelMapper.toSectionDto(updatedSection);
    }

    public void deleteSectionById(String id) {
        sectionRepository.deleteById(id);

    }

    public Section getById(String id) {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if (optionalSection.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return optionalSection.get();
    }
}
