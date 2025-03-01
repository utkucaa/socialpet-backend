package com.example.social_pet.controller;

import com.example.social_pet.dto.PetDto;
import com.example.social_pet.dto.PetRequestDto;
import com.example.social_pet.entities.AnimalType;
import com.example.social_pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pets")
public class PetController {

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
    public ResponseEntity<PetDto> createPet(@RequestBody PetRequestDto petRequestDto) {
        return new ResponseEntity<>(petService.createPet(petRequestDto), HttpStatus.CREATED);
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