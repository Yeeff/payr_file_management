package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.ConstantsDomain.AbsenceReasonsEnum;

import java.time.LocalDateTime;

public class AbsenteeismReason {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfHours;
    private AbsenceReasonsEnum absenceReasonsEnum;

    public AbsenteeismReason() {
        this.quantityOfHours = 0L;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Long getQuantityOfHours() {
        return quantityOfHours;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public AbsenceReasonsEnum getAbsenceReasonsEnum() {
        return absenceReasonsEnum;
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

    public void setAbsenceReasonsEnum(AbsenceReasonsEnum absenceReasonsEnum) {
        this.absenceReasonsEnum = absenceReasonsEnum;
    }
}
