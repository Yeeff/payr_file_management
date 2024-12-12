package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.SurchargeTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Surcharge {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfHours;
    private SurchargeTypeEnum surchargeTypeEnum;

    public void increaseOneHour(){
        quantityOfHours++;
    }

}
