package com.horizonx.file_services.adapters.driven.feign.dto;

import com.horizonx.file_services.domain.model.AbsenteeismReason;
import com.horizonx.file_services.domain.model.Overtime;
import com.horizonx.file_services.domain.model.OvertimeSurcharge;
import com.horizonx.file_services.domain.model.Surcharge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeClientResponseDto {
    private Long id;
    private String name;
    private List<Surcharge> surcharges;
    private List<Overtime> overtimes;
    private List<OvertimeSurcharge> overtimeSurcharges;
    private List<AbsenteeismReason> absenteeismReasons;

    private Double totalOvertimeSurchargeHoursNightHoliday;
    private Double totalOvertimeSurchargeHoursHoliday;
    private Double totalSurchargeHoursHoliday;
    private Double totalOvertimeHoursDay;
    private Double totalOvertimeHoursNightHoliday;
    private Double totalSurchargeHoursNightHoliday;
    private Double totalSurchargeHoursNight;
    private Double totalOvertimeHoursHoliday;
    private Double totalOvertimeHoursNight;
}
