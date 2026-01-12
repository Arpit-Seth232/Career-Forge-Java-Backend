package com.example.Carrer_backend.DTO;

import java.util.*;

public class updateMentorProfileRequest {

    private String fullName;
    private String profilePicture;
    private String yearOfMentoring;
    private String bio;
    private List<String> expertise;

    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    public String getYearOfMentoring() {
        return yearOfMentoring;
    }
    
    public void setYearOfMentoring(String yearOfMentoring) {
        this.yearOfMentoring = yearOfMentoring;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public List<String> getExpertise() {
        return expertise;
    }
    
    public void setExpertise(List<String> expertise) {
        this.expertise = expertise;
    }
    
}
