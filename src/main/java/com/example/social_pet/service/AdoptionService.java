package com.example.social_pet.service;

import com.example.social_pet.dto.AdoptionRequestDTO;
import com.example.social_pet.entities.Adoption;
import com.example.social_pet.entities.ApprovalStatus;
import com.example.social_pet.entities.User;
import com.example.social_pet.repository.AdoptionRepository;
import com.example.social_pet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AdoptionService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionService.class);

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
        try {
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
            adoption.setUser(user);
            adoption.setViewCount(0);
            adoption.setSlug(generateSlug(adoptionRequest.getTitle()));

            // Resim yükleme işlemi - eğer resim varsa
            if (adoptionRequest.getImageUrl() != null && !adoptionRequest.getImageUrl().isEmpty()) {
                String fileName = fileStorageService.storeFile(adoptionRequest.getImageUrl());
                String fileUrl = "/api/v1/files/" + fileName;
                adoption.setImageUrl(fileUrl);
            }

            if (adoptionRequest.getCreatedAt() == null) {
                adoption.setCreatedAt(LocalDate.now().atStartOfDay());
            } else {
                adoption.setCreatedAt(adoptionRequest.getCreatedAt().atStartOfDay());
            }

            Adoption savedAdoption = adoptionRepository.save(adoption);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAdoption);
        } catch (Exception e) {
            throw new RuntimeException("Error creating adoption: " + e.getMessage());
        }
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove all non-alphanumeric characters except spaces and hyphens
                .replaceAll("\\s+", "-") // Replace spaces with hyphens
                .replaceAll("-+", "-") // Replace multiple hyphens with single hyphen
                + "-" + UUID.randomUUID().toString().substring(0, 8); // Add unique identifier
    }

    public ResponseEntity<Adoption> getBySlug(String slug) {
        logger.info("Looking for adoption with slug: {}", slug);
        
        try {
            Adoption adoption = adoptionRepository.findBySlug(slug)
                    .orElseThrow(() -> new RuntimeException("Adoption not found with slug: " + slug));
            
            logger.info("Found adoption: {}", adoption.getId());
            incrementViewCount(adoption);
            return ResponseEntity.ok(adoption);
        } catch (Exception e) {
            logger.error("Error finding adoption with slug {}: {}", slug, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public void uploadPhoto(Long adoptionId, MultipartFile photo) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new RuntimeException("Adoption not found"));

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileStorageService.storeFile(photo);
            String fileUrl = "/api/v1/files/" + fileName;
            adoption.setImageUrl(fileUrl);
            adoptionRepository.save(adoption);
        } else {
            throw new RuntimeException("Photo file is required");
        }
    }

    @Transactional
    public void incrementViewCount(Adoption adoption) {
        if (adoption.getViewCount() == null) {
            adoption.setViewCount(1);
        } else {
            adoption.setViewCount(adoption.getViewCount() + 1);
        }
        adoptionRepository.save(adoption);
    }

    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findByApprovalStatus(ApprovalStatus.APPROVED);
    }
    
    public List<Adoption> getAllAdoptionsForAdmin() {
        return adoptionRepository.findAll(); // Admin tüm ilanları görebilir (pending, approved, rejected)
    }
    
    public List<Adoption> getRecentAds() {
        List<Adoption> adoptions = adoptionRepository.findByApprovalStatus(ApprovalStatus.APPROVED);
        return adoptions;
    }
    
    public Adoption getById(Long id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
    }
    
    public List<Adoption> getPendingAdoptions() {
        return adoptionRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }
    
    @Transactional
    public Adoption approveAdoption(Long id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
        adoption.setApprovalStatus(ApprovalStatus.APPROVED);
        return adoptionRepository.save(adoption);
    }
    
    @Transactional
    public Adoption rejectAdoption(Long id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
        adoption.setApprovalStatus(ApprovalStatus.REJECTED);
        return adoptionRepository.save(adoption);
    }
}