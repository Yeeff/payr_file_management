package com.horizonx.file_services.adapters.driving.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeOvertimeRequestDto {
    private List<EmployeeOvertimeDto> employees;
}