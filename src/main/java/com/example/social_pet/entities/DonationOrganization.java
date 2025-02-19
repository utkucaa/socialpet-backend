package com.example.social_pet.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "donation_organizations")
public class DonationOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phoneNumber;
    private String Iban;
}
