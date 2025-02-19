package com.example.social_pet.repository;

import com.example.social_pet.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Aktif petleri getirmek için

    // Kullanıcıya ait petleri getirmek için
    default List<Pet> findByOwnerId(Long userId) {
        return null;
    }
}
