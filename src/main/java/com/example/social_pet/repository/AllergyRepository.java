package com.example.social_pet.repository;

import com.example.social_pet.entities.Allergy;
import com.example.social_pet.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    List<Allergy> findByPet(Pet pet);
    List<Allergy> findByPetId(Long petId);
} 