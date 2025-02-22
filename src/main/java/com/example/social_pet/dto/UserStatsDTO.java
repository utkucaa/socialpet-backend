package com.example.social_pet.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserStatsDTO {
    private long totalAds;    // Total number of ads
    private long activeAds;   // Number of active ads
    private long views;       // Total views
    private Date joinDate;    // Membership date
    private String avatarUrl; // Profile photo URL
    
    // Constructor
    public UserStatsDTO(long totalAds, long activeAds, long views, Date joinDate, String avatarUrl) {
        this.totalAds = totalAds;
        this.activeAds = activeAds;
        this.views = views;
        this.joinDate = joinDate;
        this.avatarUrl = avatarUrl;
    }
    
    // Default constructor
    public UserStatsDTO() {}
}
