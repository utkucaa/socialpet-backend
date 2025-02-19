package com.example.social_pet.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date visitDate;
    private String diagnosis;
    private String treatment;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}
