package com.maxiaseo.accounting.adapters.driving.http.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FileResponseDto {
    private Long id;
    private String name;
    private LocalDateTime uploadTime;
    private String uploadedBy;
    private LocalDate fortNightDate;
}
