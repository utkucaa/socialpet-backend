package com.example.social_pet.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;

import java.util.List;

@Entity
@Table(name = "pets")
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String gender;


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false)
    private Breed breed;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<VaccinationRecord> vaccinations = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<BreedPrediction> breedPredictions = new ArrayList<>();

}
