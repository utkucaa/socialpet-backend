package com.example.social_pet.controller;

import com.example.social_pet.dto.DonationOrganizationDTO;
import com.example.social_pet.service.DonationOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donation-organizations")
public class DonationOrganizationController {

    @Autowired
    private DonationOrganizationService donationOrganizationService;

    @GetMapping
    public ResponseEntity<List<DonationOrganizationDTO>> getAllOrganizations() {
        return ResponseEntity.ok(donationOrganizationService.getAllOrganizations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationOrganizationDTO> getOrganizationById(@PathVariable Long id) {
        return ResponseEntity.ok(donationOrganizationService.getOrganizationById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DonationOrganizationDTO> createOrganization(@RequestBody DonationOrganizationDTO organizationDTO) {
        return ResponseEntity.ok(donationOrganizationService.createOrganization(organizationDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DonationOrganizationDTO> updateOrganization(
            @PathVariable Long id,
            @RequestBody DonationOrganizationDTO organizationDTO) {
        return ResponseEntity.ok(donationOrganizationService.updateOrganization(id, organizationDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        donationOrganizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
} 