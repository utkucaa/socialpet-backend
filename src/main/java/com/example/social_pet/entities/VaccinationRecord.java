package com.example.social_pet.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "vaccinations")
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vaccineName;
    private Date vaccinationDate;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}
