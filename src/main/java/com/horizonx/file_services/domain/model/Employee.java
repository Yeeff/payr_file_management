package com.horizonx.file_services.domain.model;

import com.horizonx.file_services.domain.util.ConstantsDomain.OvertimeSurchargeTypeEnum;
import com.horizonx.file_services.domain.util.ConstantsDomain.OvertimeTypeEnum;
import com.horizonx.file_services.domain.util.ConstantsDomain.SurchargeTypeEnum;

import java.util.ArrayList;
import java.util.List;


public class Employee {
    private Long id;
    private String name;
    private List<Surcharge> surcharges;
    private List<Overtime> overtimes;
    private List<AbsenteeismReason> absenteeismReasons;

    public Employee() {
        this.surcharges = new ArrayList<>();
        this.overtimes = new ArrayList<>();
        this.absenteeismReasons =new ArrayList<>();
    }

    public void addNewSurcharge(Surcharge surcharge){
        surcharges.add(surcharge);
    }

    public void addNewOverTime(Overtime overtime){
        overtimes.add(overtime);
    }

    public void addNewAbsenteeismReason(AbsenteeismReason absenteeismReason) {
        absenteeismReasons.add(absenteeismReason);
    }



    public Double getTotalSurchargeHoursNight() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.NIGHT);
    }

    public Double getTotalSurchargeHoursHoliday() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.HOLIDAY);
    }

    public Double getTotalSurchargeHoursNightHoliday() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.NIGHT_HOLIDAY);
    }


    public Double getTotalOvertimeSurchargeHoursNightHoliday() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.OVERTIME_NIGHT_HOLIDAY);

    }
    public Double getTotalOvertimeSurchargeHoursHoliday() {
        return getSumOfSurchargeHoursByType(SurchargeTypeEnum.OVERTIME_HOLIDAY);

    }



    public Double getTotalOvertimeHoursDay() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.DAY);
    }

    public Double getTotalOvertimeHoursNight() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.NIGHT);
    }

    public Double getTotalOvertimeHoursHoliday() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.HOLIDAY);
    }

    public Double getTotalOvertimeHoursNightHoliday() {
        return getSumOfOvertimeHoursByType(OvertimeTypeEnum.NIGHT_HOLIDAY);
    }





    private Double getSumOfSurchargeHoursByType(SurchargeTypeEnum type){
        Long totalMinutes = surcharges.stream()
                .filter(surcharge -> surcharge.getSurchargeTypeEnum() == type)
                .mapToLong(Surcharge::getQuantityOfMinutes)
                .sum();
        return totalMinutes / 60.0;
    }

    private Double getSumOfOvertimeHoursByType(OvertimeTypeEnum type){
        Long totalMinutes = overtimes.stream()
                .filter(overtime -> overtime.getOvertimeTypeEnum() == type)
                .mapToLong(Overtime::getQuantityOfMinutes)
                .sum();
        return totalMinutes / 60.0;
    }


    public List<AbsenteeismReason> getAbsenteeismReasons() {
        return absenteeismReasons;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Surcharge> getSurcharges() {
        return surcharges;
    }

    public List<Overtime> getOvertimes() {
        return overtimes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurcharges(List<Surcharge> surcharges) {
        this.surcharges = surcharges;
    }

    public void setOvertimes(List<Overtime> overtimes) {
        this.overtimes = overtimes;
    }

}
