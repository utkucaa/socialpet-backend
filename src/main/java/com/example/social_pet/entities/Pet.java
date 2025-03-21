package com.example.social_pet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "animal_type")
    private AnimalType animalType;
    
    @Column(name = "is_active")
    private Boolean isActive = true;

    public Boolean getIsActive() {
        return isActive;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false)
    @JsonBackReference
    private Breed breed;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BreedPrediction> breedPredictions = new ArrayList<>();
    
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Treatment> treatments = new ArrayList<>();
    
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Vaccination> vaccinations = new ArrayList<>();
    
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Allergy> allergies = new ArrayList<>();
    
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Appointment> appointments = new ArrayList<>();
    
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<WeightRecord> weightRecords = new ArrayList<>();
    
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Medication> medications = new ArrayList<>();
}
