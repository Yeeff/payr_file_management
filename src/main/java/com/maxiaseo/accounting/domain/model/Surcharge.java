package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.ConstantsDomain.SurchargeTypeEnum;

import java.time.LocalDateTime;

public class Surcharge {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfHours;
    private SurchargeTypeEnum surchargeTypeEnum;

    public Surcharge() {
        this.quantityOfHours = 0L;
    }

    public Surcharge(SurchargeTypeEnum surchargeType, Long quantityOfHours) {
        this.surchargeTypeEnum = surchargeType;
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

    public SurchargeTypeEnum getSurchargeTypeEnum() {
        return surchargeTypeEnum;
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

    public void setSurchargeTypeEnum(SurchargeTypeEnum surchargeTypeEnum) {
        this.surchargeTypeEnum = surchargeTypeEnum;
    }
}
