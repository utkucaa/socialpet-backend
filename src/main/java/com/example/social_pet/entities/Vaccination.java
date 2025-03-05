package com.example.social_pet.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vaccinations")
public class Vaccination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vaccine_name")
    private String vaccineName;
    
    @Column(name = "vaccination_date")
    private LocalDate vaccinationDate;
    
    @Column(name = "veterinarian")
    private String veterinarian;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", nullable = false)
    @JsonIgnoreProperties({"vaccinations", "treatments", "appointments", "medications", "allergies", "weightRecords"})
    private MedicalRecord medicalRecord;
    
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    @JsonIgnoreProperties({"vaccinations", "medicalRecords", "breedPredictions"})
    private Pet pet;

    public Vaccination() {}

    public Vaccination(Long id, String vaccineName, LocalDate vaccinationDate, String veterinarian) {
        this.id = id;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.veterinarian = veterinarian;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
    
    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
