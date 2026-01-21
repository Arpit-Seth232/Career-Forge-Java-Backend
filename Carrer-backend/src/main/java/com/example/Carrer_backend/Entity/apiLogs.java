package com.example.Carrer_backend.Entity;

import java.time.LocalDate;
import java.time.LocalTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "apilogs")
public class apiLogs {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "api_hit_name" , nullable = false)
    private String apiHitName;

    @Column(name = "api_hit_response" , nullable = false)
    private String apiHitResponse;

    @Column(name = "api_hit_date" , nullable = false, updatable = false)
    private LocalDate apiHitDate;

    @Column(name = "api_hit_time" , nullable = false, updatable = false)
    private LocalTime apiHitTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApiHitName() {
        return apiHitName;
    }

    public void setApiHitName(String apiHitName) {
        this.apiHitName = apiHitName;
    }

    public String getApiHitResponse() {
        return apiHitResponse;
    }

    public void setApiHitResponse(String apiHitResponse) {
        this.apiHitResponse = apiHitResponse;
    }

    public LocalDate getApiHitDate() {
        return apiHitDate;
    }

    public void setApiHitDate(LocalDate apiHitDate) {
        this.apiHitDate = apiHitDate;
    }

    public LocalTime getApiHitTime() {
        return apiHitTime;
    }

    public void setApiHitTime(LocalTime apiHitTime) {
        this.apiHitTime = apiHitTime;
    }

    @PrePersist
    protected void onCreate() {
        this.apiHitDate = LocalDate.now();
        this.apiHitTime = LocalTime.now();
    }
}
