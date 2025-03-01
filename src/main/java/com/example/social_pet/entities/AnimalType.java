package com.example.social_pet.entities;

public enum AnimalType {
    BIRD("Kuş"),
    DOG("Köpek"),
    CAT("Kedi");
    
    private final String displayName;
    
    AnimalType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 