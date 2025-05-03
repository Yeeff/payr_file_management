package com.horizonx.file_services.domain.model;

import com.horizonx.file_services.domain.util.ConstantsDomain;
import com.horizonx.file_services.domain.util.ConstantsDomain.OvertimeSurchargeTypeEnum;

import java.time.LocalDateTime;

public class OvertimeSurcharge {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfMinutes;
    private OvertimeSurchargeTypeEnum overtimeSurchargeTypeEnum;

    public OvertimeSurcharge() {
        this.quantityOfMinutes = 0L;
    }

    public OvertimeSurcharge(OvertimeSurchargeTypeEnum overtimeSurchargeType, Long quantityOfMinutes) {
        this.overtimeSurchargeTypeEnum = overtimeSurchargeType;
        this.quantityOfMinutes = quantityOfMinutes;
    }

    public void increaseValueOfStep(){
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

    public OvertimeSurchargeTypeEnum getOvertimeSurchargeTypeEnum() {
        return overtimeSurchargeTypeEnum;
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

    public void setOvertimeSurchargeTypeEnum(OvertimeSurchargeTypeEnum overtimeSurchargeTypeEnum) {
        this.overtimeSurchargeTypeEnum = overtimeSurchargeTypeEnum;
    }
}
