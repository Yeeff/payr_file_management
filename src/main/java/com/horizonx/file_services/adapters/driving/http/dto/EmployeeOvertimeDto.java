package com.horizonx.file_services.adapters.driving.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeOvertimeDto {
    private long id;
    private String name;
    private double totalSurchargeHoursNight;
    private double totalSurchargeHoursHoliday;
    private double totalSurchargeHoursNightHoliday;
    private double totalOvertimeSurchargeHoursNightHoliday;
    private double totalOvertimeSurchargeHoursHoliday;
    private double totalOvertimeHoursDay;
    private double totalOvertimeHoursNight;
    private double totalOvertimeHoursHoliday;
    private double totalOvertimeHoursNightHoliday;
}