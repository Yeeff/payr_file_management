package com.maxiaseo.accounting.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FileModel {

    private Long id;
    private String name;
    private LocalDateTime uploadTime;
    private String uploadedBy;
    private LocalDate fortNightDate;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public LocalDate getFortNightDate() {
        return fortNightDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public void setFortNightDate(LocalDate fortNightDate) {
        this.fortNightDate = fortNightDate;
    }
}
