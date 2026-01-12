package com.example.Carrer_backend.DTO;

import java.util.*;

public class updateJobseekerProfileRequest {
    
    private String fullName;
    private String profilePicture;
    private String yearOfExperience;
    private String bio;
    private List<String> preferredRoles;

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
    
    public String getYearOfExperience() {
        return yearOfExperience;
    }
    
    public void setYearOfExperience(String yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public List<String> getPreferredRoles() {
        return preferredRoles;
    }
    
    public void setPreferredRoles(List<String> preferredRoles) {
        this.preferredRoles = preferredRoles;
    }
    
}
