package com.example.social_pet.repository;

import com.example.social_pet.entities.Vaccination;
import com.example.social_pet.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    List<Vaccination> findByPet(Pet pet);
    List<Vaccination> findByPetId(Long petId);
} 