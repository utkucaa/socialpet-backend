package com.example.social_pet.dto;

import com.example.social_pet.entities.User;
import com.example.social_pet.entities.Pet;
import com.example.social_pet.entities.Question;
import com.example.social_pet.entities.Answer;
import com.example.social_pet.entities.Adoption;

import java.util.Date;
import java.util.List;

public class UserCardDto {
    private Long id;

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date joinDate;
    private List<Long> petIds;
    private List<Long> questionIds;
    private List<Long> answerIds;
    private List<Long> adoptionIds;

    public UserCardDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.joinDate = user.getJoinDate();
        this.petIds = user.getPets().stream().map(Pet::getId).collect(java.util.stream.Collectors.toList());
        this.questionIds = user.getQuestions().stream().map(Question::getId).collect(java.util.stream.Collectors.toList());
        this.answerIds = user.getAnswers().stream().map(Answer::getId).collect(java.util.stream.Collectors.toList());
        this.adoptionIds = user.getAdoptions().stream().map(Adoption::getId).collect(java.util.stream.Collectors.toList());
    }

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

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public List<Long> getPetIds() {
        return petIds;
    }

    public void setPetIds(List<Long> petIds) {
        this.petIds = petIds;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }

    public List<Long> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(List<Long> answerIds) {
        this.answerIds = answerIds;
    }

    public List<Long> getAdoptionIds() {
        return adoptionIds;
    }

    public void setAdoptionIds(List<Long> adoptionIds) {
        this.adoptionIds = adoptionIds;

    }

}

