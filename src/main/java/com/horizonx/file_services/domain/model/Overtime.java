package com.horizonx.file_services.domain.model;

import com.horizonx.file_services.domain.util.ConstantsDomain;

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

    public Long getQuantityOfMinutes() {
        return quantityOfMinutes;
    }

    public ConstantsDomain.OvertimeTypeEnum getOvertimeTypeEnum() {
        return overtimeTypeEnum;
    }


}
