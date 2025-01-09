package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.ConstantsDomain;

import java.time.LocalDateTime;


public class Overtime {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfHours;
    private ConstantsDomain.OvertimeTypeEnum overtimeTypeEnum;

    public Overtime() {
        this.quantityOfHours = 0L;
    }

    public Overtime(ConstantsDomain.OvertimeTypeEnum overtimeType, Long quantityOfHours) {
        this.overtimeTypeEnum = overtimeType;
        this.quantityOfHours = quantityOfHours;
    }


    public void increaseOneHour(){
        quantityOfHours++;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Long getQuantityOfHours() {
        return quantityOfHours;
    }

    public ConstantsDomain.OvertimeTypeEnum getOvertimeTypeEnum() {
        return overtimeTypeEnum;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setQuantityOfHours(Long quantityOfHours) {
        this.quantityOfHours = quantityOfHours;
    }

    public void setOvertimeTypeEnum(ConstantsDomain.OvertimeTypeEnum overtimeTypeEnum) {
        this.overtimeTypeEnum = overtimeTypeEnum;
    }
}
