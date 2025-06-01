package com.example.social_pet.controller;

import com.example.social_pet.dto.PetDto;
import com.example.social_pet.dto.PetRequestDto;
import com.example.social_pet.entities.AnimalType;
import com.example.social_pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);
    
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<List<PetDto>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        PetDto petDto = petService.getPetById(id);
        if (petDto != null) {
            return ResponseEntity.ok(petDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<PetDto>> getPetsByOwnerId(@PathVariable Long ownerId) {
        return ResponseEntity.ok(petService.getPetsByOwnerId(ownerId));
    }

    @PostMapping
    public ResponseEntity<?> createPet(@Valid @RequestBody PetRequestDto petRequestDto, BindingResult bindingResult) {
        try {
            logger.info("🚀 Pet oluşturma isteği alındı");
            logger.info("📋 Raw Data -> Name: '{}', Age: {}, Gender: '{}', AnimalType: '{}', OwnerId: {}, BreedId: {}", 
                petRequestDto.getName(),
                petRequestDto.getAge(),
                petRequestDto.getGender(),
                petRequestDto.getAnimalType(),
                petRequestDto.getOwnerId(),
                petRequestDto.getBreedId()
            );
            
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.toList());
                logger.error("❌ Validation hataları: {}", errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Validation failed: " + String.join(", ", errors), "status", 400));
            }
            
            PetDto createdPet = petService.createPet(petRequestDto);
            return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("💥 Pet oluşturma hatası: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage(), "status", 500));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetRequestDto petRequestDto) {
        return ResponseEntity.ok(petService.updatePet(id, petRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
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
} 