package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.utils.OvertimeTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Overtime {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfHours;
    private OvertimeTypeEnum overtimeTypeEnum;

    public void increaseOneHour(){
        quantityOfHours++;
    }
}
