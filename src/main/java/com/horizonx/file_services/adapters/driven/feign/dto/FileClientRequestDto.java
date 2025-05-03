package com.horizonx.file_services.adapters.driven.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FileClientRequestDto {
    private Long id;
    private String name;
    private LocalDateTime uploadTime;
    private String uploadedBy;
    private LocalDate fortNightDate;
    private String timeFormat;
    private List<List<String>> content;
}
