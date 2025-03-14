package com.example.social_pet.dto;

import com.example.social_pet.entities.Role;
import com.example.social_pet.entities.User;
import com.example.social_pet.entities.ApprovalStatus;

import java.util.Date;

public class AdminUserDTO {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private Role role;
    private ApprovalStatus approvalStatus;
    private Date joinDate;
    private int petCount;
    private int questionCount;
    private int answerCount;
    private int adoptionCount;
    private int lostPetCount;

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.avatarUrl = user.getAvatarUrl();
        this.role = user.getRole();
        this.approvalStatus = user.getApprovalStatus();
        this.joinDate = user.getJoinDate();
        this.petCount = user.getPets() != null ? user.getPets().size() : 0;
        this.questionCount = user.getQuestions() != null ? user.getQuestions().size() : 0;
        this.answerCount = user.getAnswers() != null ? user.getAnswers().size() : 0;
        this.adoptionCount = user.getAdoptions() != null ? user.getAdoptions().size() : 0;
        this.lostPetCount = user.getLostPets() != null ? user.getLostPets().size() : 0;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public int getPetCount() {
        return petCount;
    }

    public void setPetCount(int petCount) {
        this.petCount = petCount;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getAdoptionCount() {
        return adoptionCount;
    }

    public void setAdoptionCount(int adoptionCount) {
        this.adoptionCount = adoptionCount;
    }

    public int getLostPetCount() {
        return lostPetCount;
    }

    public void setLostPetCount(int lostPetCount) {
        this.lostPetCount = lostPetCount;
    }
} 