package com.horizonx.file_services.domain.model;

import com.horizonx.file_services.domain.exception.MissingRequiredFileModelFieldException;
import com.horizonx.file_services.domain.util.ConstantsDomain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class FileModel {

    private Long id;
    private String name;
    private LocalDateTime uploadTime;
    private String uploadedBy;
    private LocalDate fortNightDate;
    private String timeFormat;
    private Integer formId;
    private List<List<String>> content;

    public FileModel(String name, LocalDateTime uploadTime, LocalDate fortNightDate, String timeFormat, Integer formId) {

        this.name = validateRequiredField(name, ConstantsDomain.FileFields.NAME.name());
        this.uploadTime = validateRequiredField(uploadTime, ConstantsDomain.FileFields.UPLOAD_TIME.name());
        this.fortNightDate = validateRequiredField(fortNightDate, ConstantsDomain.FileFields.FORTNIGHT_DATE.name());
        this.timeFormat = validateRequiredField(timeFormat, ConstantsDomain.FileFields.TIME_FORMAT.name());
        this.formId = formId;
    }

    // Validation method
    private <T> T validateRequiredField(T value, String fieldName) {
        if (value == null) {
            throw new MissingRequiredFileModelFieldException(fieldName );
        }
        return value;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

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
