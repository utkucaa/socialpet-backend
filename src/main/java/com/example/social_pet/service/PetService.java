package com.example.social_pet.service;

import com.example.social_pet.dto.PetDTO;
import com.example.social_pet.entities.Pet;
import com.example.social_pet.exception.ResourceNotFoundException;
import com.example.social_pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    // pet'i ID'sine göre getirme
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id " + petId));
    }

    // pet'i DTO formatında almak
    public PetDTO getPetDetails(Long petId) {
        Pet pet = getPetById(petId);

        // pet detaylarını DTO'ya dönüştürüp döndürme
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setName(pet.getName());
        petDTO.setBreed(pet.getBreed().toString());
        petDTO.setAge(pet.getAge());
        petDTO.setOwnerId(pet.getOwner().getId()); // pet'in sahibinin ID'sini almak

        return petDTO;
    }

    // yeni pet oluşturma
    public Pet createPet(Pet newPet) {
        // burada, yeni pet oluşturulmadan önce gerekli doğrulama veya iş kuralları eklenebilir
        return petRepository.save(newPet);
    }

    // pet güncelleme
    public Pet updatePet(Long petId, Pet updatedPet) {
        Pet existingPet = getPetById(petId);

        // pet bilgilerini güncelleme
        existingPet.setName(updatedPet.getName());
        existingPet.setBreed(updatedPet.getBreed());
        existingPet.setAge(updatedPet.getAge());

        // güncellenmiş pet'i kaydet
        return petRepository.save(existingPet);
    }

    // pet silme
    public void deletePet(Long petId) {
        Pet pet = getPetById(petId);

        // pet'i silme
        petRepository.delete(pet);
    }

    // kullanıcının sahip olduğu tüm petleri listeleme
    public List<PetDTO> getPetsByUserId(Long userId) {
        List<Pet> pets = petRepository.findByOwnerId(userId);

        return pets.stream()
                .map(pet -> getPetDetails(pet.getId()))  // pet detaylarını DTO'ya dönüştürme
                .collect(Collectors.toList());
    }

    // aktif petleri listeleme

}
