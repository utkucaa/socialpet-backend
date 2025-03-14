package com.example.social_pet.dto;

import com.example.social_pet.entities.DonationOrganization;

public class DonationOrganizationDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String iban;
    private String website;
    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;
    private String imageUrl;
    private String description;
    private boolean active;
    
    public DonationOrganizationDTO() {
    }
    
    public DonationOrganizationDTO(DonationOrganization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.address = organization.getAddress();
        this.phoneNumber = organization.getPhoneNumber();
        this.iban = organization.getIban();
        this.website = organization.getWebsite();
        this.facebookUrl = organization.getFacebookUrl();
        this.twitterUrl = organization.getTwitterUrl();
        this.instagramUrl = organization.getInstagramUrl();
        this.imageUrl = organization.getImageUrl();
        this.description = organization.getDescription();
        this.active = organization.isActive();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
} 