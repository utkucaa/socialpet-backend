package com.example.social_pet.repository;

import com.example.social_pet.entities.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    List<Adoption> findTop5ByOrderByCreatedAtDesc();
}
