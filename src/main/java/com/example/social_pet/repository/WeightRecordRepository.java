package com.example.social_pet.repository;

import com.example.social_pet.entities.WeightRecord;
import com.example.social_pet.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
    List<WeightRecord> findByPet(Pet pet);
    List<WeightRecord> findByPetId(Long petId);
} 