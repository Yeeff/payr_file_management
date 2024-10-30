package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.utils.OvertimeSurchargeTypeEnum;
import com.maxiaseo.accounting.utils.OvertimeTypeEnum;
import com.maxiaseo.accounting.utils.SurchargeTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Employee {
    private Long id;
    private String name;
    private List<Surcharge> surcharges;
    private List<Overtime> overtimes;
    private List<OvertimeSurcharge> overtimeSurcharges;

    public void addNewSurcharge(Surcharge surcharge){
        surcharges.add(surcharge);
    }

    public void addNewOverTime(Overtime overtime){
        overtimes.add(overtime);
    }

    public void addNewOverTimeSurcharge(OvertimeSurcharge overtimeSurcharge){
        overtimeSurcharges.add(overtimeSurcharge);
    }



    public Long getTotalNightSurchargeHours() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.NIGHT);
    }

    public Long getTotalHolidaySurchargeHours() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.HOLIDAY);
    }

    public Long getTotalNightHolidaySurchargeHours() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.NIGHT_HOLIDAY);
    }



    public Long getTotalDayOvertimeHours() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.DAY);
    }

    public Long getTotalNightOvertimeHours() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.NIGHT);

    }

    public Long getTotalHolidayOvertimeHours() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.HOLIDAY);

    }

    public Long getTotalNightHolidayOvertimeHours() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.NIGHT_HOLIDAY);

    }




    public Long getTotalNightHolidayOvertimeSurchargeHours() {
        return getSumOfOvertimeSurchargeHoursByType(OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY);

    }
    public Long getTotalHolidayOvertimeSurchargeHours() {
        return getSumOfOvertimeSurchargeHoursByType(OvertimeSurchargeTypeEnum.HOLIDAY);

    }




    private Long getSumOfOvertimeSurchargeHoursByType(OvertimeSurchargeTypeEnum type){
        return overtimeSurcharges.stream()
                .filter(overtimeSurcharge -> overtimeSurcharge.getOvertimeSurchargeTypeEnum() == type)
                .mapToLong(OvertimeSurcharge::getQuantityOfHours)
                .sum();
    }

    private Long getSumOfSurchargeHoursByType(SurchargeTypeEnum type){
        return surcharges.stream()
                .filter(surcharge -> surcharge.getSurchargeTypeEnum() == type)
                .mapToLong(Surcharge::getQuantityOfHours)
                .sum();
    }

    private Long getSumOfOvertimeHoursByType(OvertimeTypeEnum type){
        return overtimes.stream()
                .filter(overtime -> overtime.getOvertimeTypeEnum() == type)
                .mapToLong(Overtime::getQuantityOfHours)
                .sum();
    }
}