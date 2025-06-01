package com.example.social_pet.service;

import com.example.social_pet.dto.PetDto;
import com.example.social_pet.dto.PetRequestDto;
import com.example.social_pet.entities.Breed;
import com.example.social_pet.entities.Pet;
import com.example.social_pet.entities.User;
import com.example.social_pet.repository.BreedRepository;
import com.example.social_pet.repository.PetRepository;
import com.example.social_pet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final BreedRepository breedRepository;

    @Autowired
    public PetService(PetRepository petRepository, UserRepository userRepository, BreedRepository breedRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.breedRepository = breedRepository;
    }

    public List<PetDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PetDto getPetById(Long id) {
        return petRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    public List<PetDto> getPetsByOwnerId(Long ownerId) {
        return petRepository.findByOwnerId(ownerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PetDto createPet(PetRequestDto petRequestDto) {
        logger.info("🔍 Pet oluşturma isteği alındı: {}", petRequestDto);
        logger.info("📋 OwnerId: {}, BreedId: {}, Name: {}, Age: {}, Gender: {}, AnimalType: {}", 
            petRequestDto.getOwnerId(), 
            petRequestDto.getBreedId(),
            petRequestDto.getName(),
            petRequestDto.getAge(),
            petRequestDto.getGender(),
            petRequestDto.getAnimalType());
        
        if (petRequestDto.getOwnerId() == null) {
            logger.error("❌ OwnerId null!");
            throw new RuntimeException("Owner ID is required and cannot be null");
        }
        
        if (petRequestDto.getBreedId() == null) {
            logger.error("❌ BreedId null!");
            throw new RuntimeException("Breed ID is required and cannot be null");
        }
        
        User owner = userRepository.findById(petRequestDto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        
        Breed breed = breedRepository.findById(petRequestDto.getBreedId())
                .orElseThrow(() -> new RuntimeException("Breed not found"));
        
        Pet pet = new Pet();
        pet.setName(petRequestDto.getName());
        pet.setAge(petRequestDto.getAge());
        pet.setGender(petRequestDto.getGender());
        pet.setAnimalType(petRequestDto.getAnimalType());
        pet.setOwner(owner);
        pet.setBreed(breed);
        
        logger.info("💾 Pet kaydediliyor...");
        Pet savedPet = petRepository.save(pet);
        logger.info("✅ Pet başarıyla kaydedildi: ID={}", savedPet.getId());
        return convertToDto(savedPet);
    }

    public PetDto updatePet(Long id, PetRequestDto petRequestDto) {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        
        User owner = userRepository.findById(petRequestDto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        
        Breed breed = breedRepository.findById(petRequestDto.getBreedId())
                .orElseThrow(() -> new RuntimeException("Breed not found"));
        
        existingPet.setName(petRequestDto.getName());
        existingPet.setAge(petRequestDto.getAge());
        existingPet.setGender(petRequestDto.getGender());
        existingPet.setAnimalType(petRequestDto.getAnimalType());
        existingPet.setOwner(owner);
        existingPet.setBreed(breed);
        
        Pet updatedPet = petRepository.save(existingPet);
        return convertToDto(updatedPet);
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    private PetDto convertToDto(Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .age(pet.getAge())
                .gender(pet.getGender())
                .animalType(pet.getAnimalType())
                .animalTypeName(pet.getAnimalType() != null ? pet.getAnimalType().getDisplayName() : null)
                .ownerId(pet.getOwner().getId())
                .ownerName(pet.getOwner().getUserName())
                .breedId(pet.getBreed().getId())
                .breedName(pet.getBreed().getName())
                .isActive(pet.getIsActive())
                .build();
    }
}
