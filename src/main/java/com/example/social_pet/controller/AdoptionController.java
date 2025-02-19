package com.example.social_pet.controller;

import com.example.social_pet.dto.AdoptionRequestDTO;
import com.example.social_pet.entities.Adoption;
import com.example.social_pet.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/adoption")
public class AdoptionController {

    private final AdoptionService adoptionService;

    @Autowired
    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Adoption> createAdoption(@RequestBody AdoptionRequestDTO adoptionRequest) {
        try {
            Adoption adoption = adoptionService.createAdoption(adoptionRequest).getBody();
            return ResponseEntity.status(HttpStatus.CREATED).body(adoption);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Adoption>> getRecentAds() {
        List<Adoption> recentAds = adoptionService.getRecentAds();
        return ResponseEntity.ok(recentAds);
    }
}