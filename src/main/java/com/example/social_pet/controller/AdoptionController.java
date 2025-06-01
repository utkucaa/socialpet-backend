package com.example.social_pet.controller;

import com.example.social_pet.dto.AdoptionRequestDTO;
import com.example.social_pet.entities.Adoption;
import com.example.social_pet.service.AdoptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@RestController
@RequestMapping("/api/v1/adoption")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdoptionController {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionController.class);
    
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

    @PostMapping(value = "/{id}/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile photo) {
        try {
            adoptionService.uploadPhoto(id, photo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Adoption>> getRecentAds() {
        List<Adoption> recentAds = adoptionService.getRecentAds();
        return ResponseEntity.ok(recentAds);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Adoption> getAdoptionBySlug(@PathVariable String slug) {
        logger.info("Request to get adoption by slug: {}", slug);
        try {
            ResponseEntity<Adoption> response = adoptionService.getBySlug(slug);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Successfully retrieved adoption with slug: {}", slug);
                return response;
            } else {
                logger.warn("Adoption not found with slug: {}", slug);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving adoption with slug {}: {}", slug, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Adoption> getAdoptionById(@PathVariable Long id) {
        logger.info("Request to get adoption by ID: {}", id);
        try {
            Adoption adoption = adoptionService.getById(id);
            if (adoption != null) {
                logger.info("Successfully retrieved adoption with ID: {}", id);
                return ResponseEntity.ok(adoption);
            } else {
                logger.warn("Adoption not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving adoption with ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        @JsonProperty("message")
        public String getMessage() {
            return message;
        }
    }
}