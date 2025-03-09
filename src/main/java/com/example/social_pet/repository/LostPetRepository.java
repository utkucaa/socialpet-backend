package com.example.social_pet.repository;

import com.example.social_pet.entities.LostPet;
import com.example.social_pet.entities.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostPetRepository extends JpaRepository<LostPet, Long> {
    List<LostPet> findByApprovalStatus(ApprovalStatus approvalStatus);
}
