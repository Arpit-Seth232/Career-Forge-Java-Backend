package com.example.Carrer_backend.Entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class resumeEmbeddedId {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    public resumeEmbeddedId() {
    }

    public resumeEmbeddedId(String userId, String fileName) {
        this.userId = userId;
        this.fileName = fileName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        resumeEmbeddedId that = (resumeEmbeddedId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, fileName);
    }
}
