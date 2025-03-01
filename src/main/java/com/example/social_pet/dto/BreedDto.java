package com.example.social_pet.dto;

import com.example.social_pet.entities.AnimalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreedDto {
    private Long id;
    private String name;
    private String description;
    private AnimalType animalType;
    private String animalTypeName;
} 