package com.example.gym.repository;

import com.example.gym.model.section.Section;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SectionRepository extends MongoRepository<Section, String> {

    Optional<Section> findByName(String name);
    
}
