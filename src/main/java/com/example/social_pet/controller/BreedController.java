package com.example.social_pet.controller;

import com.example.social_pet.dto.BreedDto;
import com.example.social_pet.entities.AnimalType;
import com.example.social_pet.request.BreedRequest;
import com.example.social_pet.service.BreedService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/breeds")
public class BreedController {

    private final BreedService breedService;

    @Autowired
    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping
    public ResponseEntity<List<BreedDto>> getAllBreeds() {
        return ResponseEntity.ok(breedService.getAllBreeds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BreedDto> getBreedById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(breedService.getBreedById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-animal-type/{animalType}")
    public ResponseEntity<List<BreedDto>> getBreedsByAnimalType(@PathVariable String animalType) {
        try {
            AnimalType type = AnimalType.valueOf(animalType.toUpperCase());
            return ResponseEntity.ok(breedService.getBreedsByAnimalType(type));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get breeds by animal type using query parameter
     * Example: GET /api/breeds?animalType=DOG
     */
    @GetMapping(params = "animalType")
    public ResponseEntity<?> getBreedsByAnimalTypeParam(@RequestParam String animalType) {
        try {
            AnimalType type = AnimalType.valueOf(animalType.toUpperCase());
            return ResponseEntity.ok(breedService.getBreedsByAnimalType(type));
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = Map.of(
                "error", "Invalid animal type: " + animalType,
                "validTypes", Arrays.stream(AnimalType.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "))
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @GetMapping("/animal-types")
    public ResponseEntity<List<Map<String, String>>> getAllAnimalTypes() {
        List<Map<String, String>> animalTypes = Arrays.stream(AnimalType.values())
                .map(type -> Map.of(
                        "code", type.name(),
                        "name", type.getDisplayName()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(animalTypes);
    }
    
    @PostMapping
    public ResponseEntity<BreedDto> createBreed(@Valid @RequestBody BreedRequest breedRequest) {
        BreedDto createdBreed = breedService.createBreed(breedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBreed);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BreedDto> updateBreed(@PathVariable Long id, @Valid @RequestBody BreedRequest breedRequest) {
        try {
            BreedDto updatedBreed = breedService.updateBreed(id, breedRequest);
            return ResponseEntity.ok(updatedBreed);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable Long id) {
        try {
            breedService.deleteBreed(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 