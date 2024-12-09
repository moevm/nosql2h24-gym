package com.example.gym.model.settings;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "loyalty_system")
public class LoyaltySettings {

    @Id
    private String id;
    private Double acceptanceRate;

}
