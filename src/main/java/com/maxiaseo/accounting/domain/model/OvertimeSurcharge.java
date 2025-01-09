package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.ConstantsDomain.OvertimeSurchargeTypeEnum;

import java.time.LocalDateTime;

public class OvertimeSurcharge {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfHours;
    private OvertimeSurchargeTypeEnum overtimeSurchargeTypeEnum;

    public OvertimeSurcharge() {
        this.quantityOfHours = 0L;
    }

    public OvertimeSurcharge(OvertimeSurchargeTypeEnum overtimeSurchargeType, Long quantityOfHours) {
        this.overtimeSurchargeTypeEnum = overtimeSurchargeType;
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

    public OvertimeSurchargeTypeEnum getOvertimeSurchargeTypeEnum() {
        return overtimeSurchargeTypeEnum;
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

    public void setOvertimeSurchargeTypeEnum(OvertimeSurchargeTypeEnum overtimeSurchargeTypeEnum) {
        this.overtimeSurchargeTypeEnum = overtimeSurchargeTypeEnum;
    }
}
