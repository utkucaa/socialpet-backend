package com.example.social_pet.dto;

import lombok.Data;

@Data
public class PetDTO {
    private Long id;
    private String name;
    private String breed;
    private int age;
    private boolean isActive;
    private String description;
    private Long ownerId;

}
