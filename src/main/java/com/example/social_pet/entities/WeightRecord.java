package com.example.social_pet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "weight_records")
public class WeightRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate recordDate;
    private double weight;
    private String unit;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", nullable = false)
    @JsonBackReference
    private MedicalRecord medicalRecord;

    public WeightRecord() {}

    public WeightRecord(Long id, LocalDate recordDate, double weight, String unit, String notes) {
        this.id = id;
        this.recordDate = recordDate;
        this.weight = weight;
        this.unit = unit;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
