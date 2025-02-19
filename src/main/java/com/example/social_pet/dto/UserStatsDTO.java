package com.example.social_pet.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserStatsDTO {
    private long totalAds;    // Toplam ilan sayısı
    private long activeAds;   // Aktif ilan sayısı
    private Date joinDate;    // Üye olma tarihi
}
