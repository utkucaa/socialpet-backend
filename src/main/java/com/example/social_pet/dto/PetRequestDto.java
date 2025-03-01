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
public class PetRequestDto {
    private String name;
    private int age;
    private String gender;
    private AnimalType animalType;
    private Long ownerId;
    private Long breedId;
} 