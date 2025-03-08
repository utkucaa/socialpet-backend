package com.example.social_pet.service;

import com.example.social_pet.dto.DonationOrganizationDTO;
import com.example.social_pet.entities.DonationOrganization;
import com.example.social_pet.repository.DonationOrganizationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationOrganizationService {

    @Autowired
    private DonationOrganizationRepository donationOrganizationRepository;

    private DonationOrganizationDTO convertToDTO(DonationOrganization organization) {
        DonationOrganizationDTO dto = new DonationOrganizationDTO();
        dto.setId(organization.getId());
        dto.setName(organization.getName());
        dto.setAddress(organization.getAddress());
        dto.setPhoneNumber(organization.getPhoneNumber());
        dto.setIban(organization.getIban());
        return dto;
    }

    private DonationOrganization convertToEntity(DonationOrganizationDTO dto) {
        DonationOrganization organization = new DonationOrganization();
        organization.setId(dto.getId());
        organization.setName(dto.getName());
        organization.setAddress(dto.getAddress());
        organization.setPhoneNumber(dto.getPhoneNumber());
        organization.setIban(dto.getIban());
        return organization;
    }

    public List<DonationOrganizationDTO> getAllOrganizations() {
        return donationOrganizationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DonationOrganizationDTO getOrganizationById(Long id) {
        return donationOrganizationRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
    }

    public DonationOrganizationDTO createOrganization(DonationOrganizationDTO organizationDTO) {
        DonationOrganization organization = convertToEntity(organizationDTO);
        organization = donationOrganizationRepository.save(organization);
        return convertToDTO(organization);
    }

    public DonationOrganizationDTO updateOrganization(Long id, DonationOrganizationDTO organizationDTO) {
        if (!donationOrganizationRepository.existsById(id)) {
            throw new RuntimeException("Organization not found with id: " + id);
        }
        organizationDTO.setId(id);
        DonationOrganization organization = convertToEntity(organizationDTO);
        organization = donationOrganizationRepository.save(organization);
        return convertToDTO(organization);
    }

    public void deleteOrganization(Long id) {
        if (!donationOrganizationRepository.existsById(id)) {
            throw new RuntimeException("Organization not found with id: " + id);
        }
        donationOrganizationRepository.deleteById(id);
    }
} 