package com.example.social_pet.request;

import com.example.social_pet.entities.AnimalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreedRequest {
    
    @NotBlank(message = "Breed name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Animal type is required")
    private AnimalType animalType;
} 