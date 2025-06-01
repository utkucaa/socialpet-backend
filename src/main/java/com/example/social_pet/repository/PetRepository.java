package com.example.social_pet.repository;

import com.example.social_pet.entities.AnimalType;
import com.example.social_pet.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Find active pets
    List<Pet> findByIsActiveTrue();

    // Kullanıcıya ait petleri getirmek için
    List<Pet> findByOwnerId(Long ownerId);

    List<Pet> findByAnimalType(AnimalType animalType);

    List<Pet> findByBreedId(Long breedId);
}
