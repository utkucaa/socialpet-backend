package com.example.social_pet.repository;

import com.example.social_pet.entities.Petshop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetshopRepository extends JpaRepository<Petshop, Long> {
}
