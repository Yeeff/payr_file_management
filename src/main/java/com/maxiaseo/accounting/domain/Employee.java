package com.maxiaseo.accounting.domain;

import com.maxiaseo.accounting.utils.SurchargeTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Employee {
    private Long id;
    private String name;
    private List<Surcharge> surcharges;

    public void addNewSurcharge(Surcharge surcharge){
        surcharges.add(surcharge);
    }

    public Long getTotalNightSurchargeHours() {
        return surcharges.stream()
                .filter(surcharge -> surcharge.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .mapToLong(Surcharge::getQuantityOfHours)
                .sum();
    }

    public Long getTotalHolidaySurchargeHours() {
        return surcharges.stream()
                .filter(surcharge -> surcharge.getSurchargeTypeEnum() == SurchargeTypeEnum.HOLIDAY)
                .mapToLong(Surcharge::getQuantityOfHours)
                .sum();
    }

    public Long getTotalNightHolidaySurchargeHours() {
        return surcharges.stream()
                .filter(surcharge -> surcharge.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT_HOLIDAY)
                .mapToLong(Surcharge::getQuantityOfHours)
                .sum();
    }
}
