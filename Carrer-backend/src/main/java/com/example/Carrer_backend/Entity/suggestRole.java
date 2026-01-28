package com.example.Carrer_backend.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "suggested_roles")
public class suggestRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false),
            @JoinColumn(name = "file_name", referencedColumnName = "file_name", insertable = false, updatable = false)
    })
    private resume resume;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "suggested_roles_response", nullable = false, columnDefinition = "jsonb")
    private JsonNode suggestedRolesResponse;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public JsonNode getSuggestedRolesResponse() {
        return suggestedRolesResponse;
    }

    public void setSuggestedRolesResponse(JsonNode suggestedRolesResponse) {
        this.suggestedRolesResponse = suggestedRolesResponse;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public resume getResume() {
        return resume;
    }

    public void setResume(resume resume) {
        this.resume = resume;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
