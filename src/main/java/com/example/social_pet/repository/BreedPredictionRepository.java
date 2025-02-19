package com.example.social_pet.repository;

import com.example.social_pet.entities.BreedPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedPredictionRepository extends JpaRepository<BreedPrediction, Long> {
}
