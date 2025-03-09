package com.example.social_pet.repository;

import com.example.social_pet.entities.Adoption;
import com.example.social_pet.entities.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    List<Adoption> findTop5ByOrderByCreatedAtDesc();
    Optional<Adoption> findBySlug(String slug);
    List<Adoption> findByApprovalStatus(ApprovalStatus approvalStatus);
}
