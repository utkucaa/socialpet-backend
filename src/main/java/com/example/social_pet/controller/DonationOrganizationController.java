package com.example.social_pet.controller;

import com.example.social_pet.dto.DonationOrganizationDTO;
import com.example.social_pet.response.FileUrlResponse;
import com.example.social_pet.service.DonationOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donation-organizations")
@CrossOrigin(origins = "http://localhost:3000")
public class DonationOrganizationController {

    @Autowired
    private DonationOrganizationService organizationService;
    
    // Get all organizations (admin only)
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DonationOrganizationDTO>> getAllOrganizations() {
        List<DonationOrganizationDTO> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }
    
    // Get active organizations (public)
    @GetMapping
    public ResponseEntity<List<DonationOrganizationDTO>> getActiveOrganizations() {
        List<DonationOrganizationDTO> organizations = organizationService.getActiveOrganizations();
        return ResponseEntity.ok(organizations);
    }
    
    // Get organization by ID
    @GetMapping("/{id}")
    public ResponseEntity<DonationOrganizationDTO> getOrganizationById(@PathVariable Long id) {
        try {
            DonationOrganizationDTO organization = organizationService.getOrganizationById(id);
            return ResponseEntity.ok(organization);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // Create organization (admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonationOrganizationDTO> createOrganization(@RequestBody DonationOrganizationDTO organizationDTO) {
        try {
            DonationOrganizationDTO createdOrganization = organizationService.createOrganization(organizationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrganization);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    // Update organization (admin only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonationOrganizationDTO> updateOrganization(
            @PathVariable Long id,
            @RequestBody DonationOrganizationDTO organizationDTO) {
        try {
            DonationOrganizationDTO updatedOrganization = organizationService.updateOrganization(id, organizationDTO);
            return ResponseEntity.ok(updatedOrganization);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    // Delete organization (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
        try {
            organizationService.deleteOrganization(id);
            return ResponseEntity.ok("Organization deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    // Upload organization image (admin only)
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FileUrlResponse> uploadOrganizationImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image) {
        try {
            String fileUrl = organizationService.uploadOrganizationImage(id, image);
            return ResponseEntity.ok(new FileUrlResponse(fileUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    // Toggle organization active status (admin only)
    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonationOrganizationDTO> toggleOrganizationStatus(@PathVariable Long id) {
        try {
            DonationOrganizationDTO organization = organizationService.toggleOrganizationStatus(id);
            return ResponseEntity.ok(organization);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
} 