package com.example.social_pet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "lost_pets")
@Data

public class LostPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "details", nullable = false, length = 1000)
    private String details;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "category", nullable = false)
    private String category; // kedi, köpek, muhabbet kuşu, papağan

    @Column(name = "status", nullable = false)
    private String status; // kayıp,bulundu gibi

    @Column(name = "additional_info")
    private String additionalInfo; // ödüllü, acil, boş, blabla

    @Column(name = "image_url", nullable = true, length = 1000000)
    private String imageUrl;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Column(name = "last_seen_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastSeenDate;

    @Column(name = "last_seen_location")
    private String lastSeenLocation;

    private Integer viewCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user; // İlana sahip olan kullanıcı
}

