package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.ConstantsDomain;

import java.time.LocalDateTime;


public class Overtime {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfMinutes;
    private ConstantsDomain.OvertimeTypeEnum overtimeTypeEnum;

    public Overtime() {
        this.quantityOfMinutes = 0L;
    }

    public Overtime(ConstantsDomain.OvertimeTypeEnum overtimeType, Long quantityOfMinutes) {
        this.overtimeTypeEnum = overtimeType;
        this.quantityOfMinutes = quantityOfMinutes;
    }


    public void increasValueOfStep(){
        quantityOfMinutes += ConstantsDomain.STEP_IN_MINUTES;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Long getQuantityOfMinutes() {
        return quantityOfMinutes;
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

    public void setQuantityOfMinutes(Long quantityOfMinutes) {
        this.quantityOfMinutes = quantityOfMinutes;
    }

    public void setOvertimeTypeEnum(ConstantsDomain.OvertimeTypeEnum overtimeTypeEnum) {
        this.overtimeTypeEnum = overtimeTypeEnum;
    }
}
