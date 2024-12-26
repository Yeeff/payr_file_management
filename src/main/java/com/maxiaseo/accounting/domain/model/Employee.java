package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.OvertimeSurchargeTypeEnum;
import com.maxiaseo.accounting.domain.util.OvertimeTypeEnum;
import com.maxiaseo.accounting.domain.util.SurchargeTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Employee {
    private Long id;
    private String name;
    private List<Surcharge> surcharges;
    private List<Overtime> overtimes;
    private List<OvertimeSurcharge> overtimeSurcharges;

    public Employee() {
        this.surcharges = new ArrayList<>();
        this.overtimes = new ArrayList<>();
        this.overtimeSurcharges = new ArrayList<>();
    }

    public void addNewSurcharge(Surcharge surcharge){
        surcharges.add(surcharge);
    }

    public void addNewOverTime(Overtime overtime){
        overtimes.add(overtime);
    }

    public void addNewOverTimeSurcharge(OvertimeSurcharge overtimeSurcharge){
        overtimeSurcharges.add(overtimeSurcharge);
    }



    public Long getTotalSurchargeHoursNight() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.NIGHT);
    }

    public Long getTotalSurchargeHoursHoliday() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.HOLIDAY);
    }

    public Long getTotalSurchargeHoursNightHoliday() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.NIGHT_HOLIDAY);
    }



    public Long getTotalOvertimeHoursDay() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.DAY);
    }

    public Long getTotalOvertimeHoursNight() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.NIGHT);
    }

    public Long getTotalOvertimeHoursHoliday() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.HOLIDAY);
    }

    public Long getTotalOvertimeHoursNightHoliday() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.NIGHT_HOLIDAY);
    }




    public Long getTotalOvertimeSurchargeHoursNightHoliday() {
        return getSumOfOvertimeSurchargeHoursByType(OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY);

    }
    public Long getTotalOvertimeSurchargeHoursHoliday() {
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
