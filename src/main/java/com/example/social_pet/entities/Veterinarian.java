package com.example.social_pet.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "veterinarians")
public class Veterinarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;


}
