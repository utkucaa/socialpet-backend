package com.example.social_pet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String userName;
    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.MEMBER; // Default role is MEMBER

    @JsonIgnore
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Pet> pets = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Answer> answers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Adoption> adoptions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<LostPet> lostPets = new ArrayList<>();
    private Date joinDate;

    @PrePersist
    public void onCreate() {
        this.joinDate = new Date();
    }

    // Role related methods
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    // Existing methods...
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
