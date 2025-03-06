package com.example.social_pet.dto;

public class WeightRecordRequest {
    private String recordDate;
    private Double weight;
    private String unit;
    private String notes;
    
    // Frontend'den gelen alternatif alan isimleri
    private String date;

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Frontend'den gelen alternatif alanlar için getter ve setter'lar
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
} 