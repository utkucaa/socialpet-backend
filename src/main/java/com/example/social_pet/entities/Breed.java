package com.example.social_pet.entities;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "breeds")
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "breed", cascade = CascadeType.ALL)
    private List<Pet> pets;
}
