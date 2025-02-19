package com.example.social_pet.repository;

import com.example.social_pet.entities.LostPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostPetRepository extends JpaRepository<LostPet, Long> {
}
