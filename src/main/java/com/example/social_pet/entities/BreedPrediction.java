package com.example.social_pet.entities;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "breed_predictions")
public class BreedPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String predictedBreed;
    private Double confidence;
    private Date predictionDate;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}
