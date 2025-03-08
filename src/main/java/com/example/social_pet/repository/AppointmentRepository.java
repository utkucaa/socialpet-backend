package com.example.social_pet.repository;

import com.example.social_pet.entities.Appointment;
import com.example.social_pet.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPet(Pet pet);
    List<Appointment> findByPetId(Long petId);
} 