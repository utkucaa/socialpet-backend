package com.example.social_pet.service;

import com.example.social_pet.dto.BreedDto;
import com.example.social_pet.entities.AnimalType;
import com.example.social_pet.entities.Breed;
import com.example.social_pet.repository.BreedRepository;
import com.example.social_pet.request.BreedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreedService {

    private final BreedRepository breedRepository;

    @Autowired
    public BreedService(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    public List<BreedDto> getAllBreeds() {
        return breedRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BreedDto> getBreedsByAnimalType(AnimalType animalType) {
        return breedRepository.findByAnimalType(animalType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BreedDto createBreed(BreedRequest breedRequest) {
        Breed breed = new Breed();
        breed.setName(breedRequest.getName());
        breed.setDescription(breedRequest.getDescription());
        breed.setAnimalType(breedRequest.getAnimalType());
        
        Breed savedBreed = breedRepository.save(breed);
        return convertToDto(savedBreed);
    }
    
    public BreedDto updateBreed(Long id, BreedRequest breedRequest) {
        Breed existingBreed = breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        
        existingBreed.setName(breedRequest.getName());
        existingBreed.setDescription(breedRequest.getDescription());
        existingBreed.setAnimalType(breedRequest.getAnimalType());
        
        Breed updatedBreed = breedRepository.save(existingBreed);
        return convertToDto(updatedBreed);
    }
    
    public void deleteBreed(Long id) {
        if (!breedRepository.existsById(id)) {
            throw new RuntimeException("Breed not found with id: " + id);
        }
        breedRepository.deleteById(id);
    }
    
    public BreedDto getBreedById(Long id) {
        Breed breed = breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        return convertToDto(breed);
    }

    private BreedDto convertToDto(Breed breed) {
        return BreedDto.builder()
                .id(breed.getId())
                .name(breed.getName())
                .description(breed.getDescription())
                .animalType(breed.getAnimalType())
                .animalTypeName(breed.getAnimalType().getDisplayName())
                .build();
    }
} 