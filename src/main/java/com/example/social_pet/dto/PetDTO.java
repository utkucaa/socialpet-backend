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
public class PetDto {
    private Long id;
    private String name;
    private int age;
    private String gender;
    private AnimalType animalType;
    private String animalTypeName;
    private Long ownerId;
    private String ownerName;
    private Long breedId;
    private String breedName;
    private Boolean isActive;
} 