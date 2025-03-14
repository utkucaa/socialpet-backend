package com.example.social_pet.repository;

import com.example.social_pet.entities.DonationOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationOrganizationRepository extends JpaRepository<DonationOrganization, Long> {
    List<DonationOrganization> findByActiveTrue();
} 