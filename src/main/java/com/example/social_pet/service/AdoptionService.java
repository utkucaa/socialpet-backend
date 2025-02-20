package com.example.social_pet.service;

import com.example.social_pet.dto.AdoptionRequestDTO;
import com.example.social_pet.entities.Adoption;
import com.example.social_pet.entities.User;
import com.example.social_pet.repository.AdoptionRepository;
import com.example.social_pet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdoptionService {

    @Autowired
    private final UserRepository userRepository;
    private final AdoptionRepository adoptionRepository;
    private final FileStorageService fileStorageService;

    public AdoptionService(UserRepository userRepository, AdoptionRepository adoptionRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.adoptionRepository = adoptionRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public ResponseEntity<Adoption> createAdoption(AdoptionRequestDTO adoptionRequest) {
        User user = userRepository.findById(adoptionRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Adoption adoption = new Adoption();
        adoption.setAnimalType(adoptionRequest.getAnimalType());
        adoption.setPetName(adoptionRequest.getPetName());
        adoption.setBreed(adoptionRequest.getBreed());
        adoption.setAge(adoptionRequest.getAge());
        adoption.setGender(adoptionRequest.getGender());
        adoption.setSize(adoptionRequest.getSize());
        adoption.setTitle(adoptionRequest.getTitle());
        adoption.setDescription(adoptionRequest.getDescription());
        adoption.setSource(adoptionRequest.getSource());
        adoption.setCity(adoptionRequest.getCity());
        adoption.setDistrict(adoptionRequest.getDistrict());
        adoption.setFullName(adoptionRequest.getFullName());
        adoption.setPhone(adoptionRequest.getPhone());

        if (adoptionRequest.getCreatedAt() == null) {
            adoption.setCreatedAt(LocalDate.now().atStartOfDay());
        } else {
            adoption.setCreatedAt(adoptionRequest.getCreatedAt().atStartOfDay());
        }

        adoption.setUser(user);
        Adoption newAdoption = adoptionRepository.save(adoption);
        return ResponseEntity.ok(newAdoption);
    }

    @Transactional
    public void uploadPhoto(Long adoptionId, MultipartFile photo) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new RuntimeException("Adoption not found"));

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileStorageService.storeFile(photo);
            adoption.setImageUrl(fileName);
            adoptionRepository.save(adoption);
        } else {
            throw new RuntimeException("Photo file is required");
        }
    }

    public List<Adoption> getRecentAds() {
        return (List<Adoption>) adoptionRepository.findTop5ByOrderByCreatedAtDesc();
    }
}