package com.example.social_pet.service;

import com.example.social_pet.dto.DonationOrganizationDTO;
import com.example.social_pet.entities.DonationOrganization;
import com.example.social_pet.repository.DonationOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationOrganizationService {

    @Autowired
    private DonationOrganizationRepository organizationRepository;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    // Get all organizations
    public List<DonationOrganizationDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(DonationOrganizationDTO::new)
                .collect(Collectors.toList());
    }
    
    // Get active organizations
    public List<DonationOrganizationDTO> getActiveOrganizations() {
        return organizationRepository.findByActiveTrue().stream()
                .map(DonationOrganizationDTO::new)
                .collect(Collectors.toList());
    }
    
    // Get organization by ID
    public DonationOrganizationDTO getOrganizationById(Long id) {
        DonationOrganization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        return new DonationOrganizationDTO(organization);
    }
    
    // Create organization
    public DonationOrganizationDTO createOrganization(DonationOrganizationDTO organizationDTO) {
        DonationOrganization organization = new DonationOrganization();
        updateOrganizationFromDTO(organization, organizationDTO);
        
        DonationOrganization savedOrganization = organizationRepository.save(organization);
        return new DonationOrganizationDTO(savedOrganization);
    }
    
    // Update organization
    public DonationOrganizationDTO updateOrganization(Long id, DonationOrganizationDTO organizationDTO) {
        DonationOrganization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        
        updateOrganizationFromDTO(organization, organizationDTO);
        
        DonationOrganization updatedOrganization = organizationRepository.save(organization);
        return new DonationOrganizationDTO(updatedOrganization);
    }
    
    // Delete organization
    public void deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new RuntimeException("Organization not found with id: " + id);
        }
        organizationRepository.deleteById(id);
    }
    
    // Upload organization image
    public String uploadOrganizationImage(Long id, MultipartFile image) {
        DonationOrganization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        
        if (image != null && !image.isEmpty()) {
            String fileName = fileStorageService.storeFile(image);
            String fileUrl = "/api/v1/files/" + fileName;
            
            organization.setImageUrl(fileUrl);
            organizationRepository.save(organization);
            
            return fileUrl;
        }
        
        throw new RuntimeException("Image file is required");
    }
    
    // Activate/Deactivate organization
    public DonationOrganizationDTO toggleOrganizationStatus(Long id) {
        DonationOrganization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        
        organization.setActive(!organization.isActive());
        DonationOrganization updatedOrganization = organizationRepository.save(organization);
        
        return new DonationOrganizationDTO(updatedOrganization);
    }
    
    // Helper method to update organization from DTO
    private void updateOrganizationFromDTO(DonationOrganization organization, DonationOrganizationDTO dto) {
        organization.setName(dto.getName());
        organization.setAddress(dto.getAddress());
        organization.setPhoneNumber(dto.getPhoneNumber());
        organization.setIban(dto.getIban());
        organization.setWebsite(dto.getWebsite());
        organization.setFacebookUrl(dto.getFacebookUrl());
        organization.setTwitterUrl(dto.getTwitterUrl());
        organization.setInstagramUrl(dto.getInstagramUrl());
        organization.setDescription(dto.getDescription());
        
        // Only set image URL if it's provided in the DTO
        if (dto.getImageUrl() != null) {
            organization.setImageUrl(dto.getImageUrl());
        }
        
        // Only set active status if it's provided in the DTO (for admin operations)
        if (dto.isActive() != organization.isActive()) {
            organization.setActive(dto.isActive());
        }
    }
} 