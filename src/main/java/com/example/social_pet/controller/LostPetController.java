package com.example.social_pet.controller;

import com.example.social_pet.entities.LostPet;
import com.example.social_pet.service.LostPetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lostpets")
@RequiredArgsConstructor
public class LostPetController {

    private final LostPetService lostPetService;

    @PostMapping("/{userId}")
    public ResponseEntity<LostPet> createLostPet(@RequestBody LostPet lostPet, @PathVariable Long userId) {
        // Burada, gelen userId'yi kullanarak kayıp ilanını ilişkilendirirsiniz
        return ResponseEntity.ok(lostPetService.addLostPet(lostPet, userId));
    }

    // Tüm ilanları getirme
    @GetMapping
    public ResponseEntity<List<LostPet>> getAllLostPets() {
        return ResponseEntity.ok(lostPetService.getAllLostPets());
    }

    // Belirli bir ilanı ID'ye göre getirme
    @GetMapping("/{id}")
    public ResponseEntity<LostPet> getLostPetById(@PathVariable Long id) {
        return ResponseEntity.ok(lostPetService.getLostPetById(id));
    }

    // Kullanıcı sadece kendi ilanını silebilir
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostPet(
            @PathVariable Long id,
            @RequestParam Long userId) {

        lostPetService.deleteLostPet(id, userId);
        return ResponseEntity.noContent().build();
    }
}
