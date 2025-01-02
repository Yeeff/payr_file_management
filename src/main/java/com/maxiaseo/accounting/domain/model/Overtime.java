package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.OvertimeTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class Overtime {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfHours;
    private OvertimeTypeEnum overtimeTypeEnum;

    public Overtime() {
        this.quantityOfHours = 0L;
    }

    public Overtime(OvertimeTypeEnum overtimeType, Long quantityOfHours) {
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

    public OvertimeTypeEnum getOvertimeTypeEnum() {
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

    public void setOvertimeTypeEnum(OvertimeTypeEnum overtimeTypeEnum) {
        this.overtimeTypeEnum = overtimeTypeEnum;
    }
}
