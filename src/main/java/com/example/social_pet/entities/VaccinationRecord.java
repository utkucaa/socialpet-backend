package com.example.social_pet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "vaccination_records")
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vaccineName;
    private Date vaccinationDate;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    @JsonBackReference
    private Pet pet;
}
