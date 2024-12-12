package com.maxiaseo.accounting.domain.model;

import com.maxiaseo.accounting.domain.util.OvertimeSurchargeTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OvertimeSurcharge {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfHours;
    private OvertimeSurchargeTypeEnum overtimeSurchargeTypeEnum;

    public void increaseOneHour(){
        quantityOfHours++;
    }
}
