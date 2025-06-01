package com.example.social_pet.dto;

import com.example.social_pet.entities.AnimalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetRequestDto {
    
    @NotBlank(message = "Pet name is required")
    private String name;
    
    @Min(value = 0, message = "Pet age must be 0 or greater")
    private int age;
    
    @NotBlank(message = "Pet gender is required")
    private String gender;
    
    @NotNull(message = "Animal type is required")
    private AnimalType animalType;
    
    @NotNull(message = "Owner ID is required")
    private Long ownerId;
    
    @NotNull(message = "Breed ID is required")
    private Long breedId;
} 