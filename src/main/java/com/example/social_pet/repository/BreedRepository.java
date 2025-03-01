package com.example.social_pet.repository;

import com.example.social_pet.entities.AnimalType;
import com.example.social_pet.entities.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
    List<Breed> findByAnimalType(AnimalType animalType);
}
