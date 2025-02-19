package com.example.social_pet.repository;

import com.example.social_pet.entities.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
}
