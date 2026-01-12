package com.example.Carrer_backend.Entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "mentors")
public class mentor {
    
    @Id
    @Column(name = "user_id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @MapsId
    private user PersonalDetails;

    @Column(name = "full_name", nullable = false, length = 30)
    private String fullName;

    @Column(name = "profile_picture", length = 100)
    private String profilePicture;

    @Column(name = "year_of_mentoring", length = 20, nullable = false)
    private String yearOfMentoring;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @ElementCollection
    @CollectionTable(name = "expertise", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "field", length = 100, nullable = false)
    private List<String> expertise;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true, nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public user getPersonalDetails() {
        return PersonalDetails;
    }

    public void setPersonalDetails(user personalDetails) {
        PersonalDetails = personalDetails;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
