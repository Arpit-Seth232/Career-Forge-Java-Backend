package com.example.Carrer_backend.Entity;

import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "jobseekers")
public class jobseeker {

    @Id
    @Column(name = "user_id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId
    @JsonIgnore
    private user personalDetails;

    @Column(name = "full_name", nullable = false, length = 30)
    private String fullName;

    @Column(name = "profile_picture", length = 100)
    private String profilePicture;

    @Column(name = "year_of_experience", length = 20, nullable = false)
    private String yearOfExperience;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @ElementCollection
    @CollectionTable(name = "preferred_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", length = 100, nullable = false)
    private List<String> preferredRoles;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true, nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public user getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(user personalDetails) {
        this.personalDetails = personalDetails;
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
