package com.example.social_pet.repository;

import com.example.social_pet.entities.Treatment;
import com.example.social_pet.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    List<Treatment> findByPet(Pet pet);
    List<Treatment> findByPetId(Long petId);
} 