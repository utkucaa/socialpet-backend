package com.example.social_pet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Google Places API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {
    private String placeId;
    private String name;
    private String formattedAddress;
    private String vicinity;
    private Double latitude;
    private Double longitude;
    private String icon;
    private List<String> types;
    private Double rating;
    private Integer userRatingsTotal;
    private Boolean openNow;
    private String photoReference;
    private String website;
    private String phoneNumber;
} 