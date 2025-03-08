package com.example.social_pet.dto;

public class DonationOrganizationDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String Iban;

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
        return Iban;
    }

    public void setIban(String iban) {
        this.Iban = iban;
    }
} 