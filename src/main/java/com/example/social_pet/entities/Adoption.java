package com.example.social_pet.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "adoptions")
public class Adoption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String animalType;

    @Column(nullable = false)
    private String petName;

    private String breed;
    private Integer age;
    private String gender;
    private String size;

    private Integer viewCount;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String source;
    private String city;
    private String district;
    private String fullName;
    private String phone;
    private String imageUrl;

    @Column(unique = true)
    private String slug;

    @Column(nullable = true)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setPhotoUrl(String fileUrl) {
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
