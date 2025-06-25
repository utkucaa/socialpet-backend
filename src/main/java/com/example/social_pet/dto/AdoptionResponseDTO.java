package com.example.social_pet.dto;

import com.example.social_pet.entities.Adoption;
import com.example.social_pet.entities.ApprovalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class AdoptionResponseDTO {
    private Long id;
    private String animalType;
    private String petName;
    private String breed;
    private Integer age;
    private String gender;
    private String size;
    private Integer viewCount;
    private String title;
    private String description;
    private String source;
    private String city;
    private String district;
    private String fullName;
    private String phone;
    private String imageUrl;
    private String slug;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    private ApprovalStatus approvalStatus;
    private String userName;
    private Long userId;

    // Constructor
    public AdoptionResponseDTO(Adoption adoption) {
        this.id = adoption.getId();
        this.animalType = adoption.getAnimalType();
        this.petName = adoption.getPetName();
        this.breed = adoption.getBreed();
        this.age = adoption.getAge();
        this.gender = adoption.getGender();
        this.size = adoption.getSize();
        this.viewCount = adoption.getViewCount();
        this.title = adoption.getTitle();
        this.description = adoption.getDescription();
        this.source = adoption.getSource();
        this.city = adoption.getCity();
        this.district = adoption.getDistrict();
        this.fullName = adoption.getFullName();
        this.phone = adoption.getPhone();
        this.slug = adoption.getSlug();
        // CreatedAt için null check
        this.createdAt = adoption.getCreatedAt() != null ? adoption.getCreatedAt() : java.time.LocalDateTime.now();
        this.approvalStatus = adoption.getApprovalStatus();
        
        // ImageUrl'u direkt olarak ata - null ise null olarak bırak
        this.imageUrl = adoption.getImageUrl();
        
        // User bilgileri
        if (adoption.getUser() != null) {
            this.userName = adoption.getUser().getUserName();
            this.userId = adoption.getUser().getId();
        }
    }
    


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAnimalType() { return animalType; }
    public void setAnimalType(String animalType) { this.animalType = animalType; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
