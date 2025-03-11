package com.maxiaseo.accounting.domain.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FileModel {

    private Long id;
    private String name;
    private LocalDateTime uploadTime;
    private String uploadedBy;
    private LocalDate fortNightDate;
    private String timeFormat;
    private List<List<String>> content;

    public String getTimeFormat() {
        return timeFormat;
    }
    public List<List<String>> getContent() {
        return content;
    }

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

    public void setContent(List<List<String>> content) {
        this.content = content;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }
}
