package com.maxiaseo.accounting.domain.service.processor;

import com.maxiaseo.accounting.domain.model.Surcharge;
import com.maxiaseo.accounting.domain.util.ConstantsDomain.SurchargeTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.maxiaseo.accounting.domain.util.ConstantsDomain.*;

public class SurchargeCalculator {

    private SurchargeCalculator(){}


    public static List<Surcharge> getSurchargeList(LocalDateTime start, LocalDateTime end) {

        List<Surcharge> surchargesList = new ArrayList<>();

        Surcharge surchargeNight = new Surcharge();
        Surcharge surchargeHoliday = new Surcharge();
        Surcharge surchargeHolidayNight = new Surcharge();

        LocalDateTime current = start.plusMinutes(STEP_IN_MINUTES);

        while (current.isBefore(end) || current.isEqual(end)) {

            if (getSurchargeType(current) == SurchargeTypeEnum.NIGHT) {
                surchargeNight = increaseValueOfStepToSurcharge(surchargeNight, current, SurchargeTypeEnum.NIGHT);
            }
            if (getSurchargeType(current) == SurchargeTypeEnum.HOLIDAY) {
                surchargeHoliday = increaseValueOfStepToSurcharge(surchargeHoliday, current, SurchargeTypeEnum.HOLIDAY);
            }
            if (getSurchargeType(current) == SurchargeTypeEnum.NIGHT_HOLIDAY) {
                surchargeHolidayNight = increaseValueOfStepToSurcharge(surchargeHolidayNight, current, SurchargeTypeEnum.NIGHT_HOLIDAY);
            }

            LocalDateTime maxTimeToCheckSurcharges = start.plusHours(MAX_HOURS_BY_DAY );
            if(maxTimeToCheckSurcharges.isEqual(current)) break;

            current = current.plusMinutes(STEP_IN_MINUTES);
        }

        if (surchargeNight.getQuantityOfMinutes() != 0) surchargesList.add(surchargeNight);
        if (surchargeHoliday.getQuantityOfMinutes() != 0) surchargesList.add(surchargeHoliday);
        if (surchargeHolidayNight.getQuantityOfMinutes() != 0) surchargesList.add(surchargeHolidayNight);

        return surchargesList;
    }

    private static SurchargeTypeEnum getSurchargeType(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        if(time.equals(LocalTime.of(0,0))) {
            dateTime =dateTime.minusMinutes(1);
        }
        DayOfWeek day = dateTime.getDayOfWeek();

        boolean isNightSurcharge = (time.isAfter(NIGHT_START) || time.equals(NIGHT_END)) || time.isBefore(NIGHT_END);
        boolean isSunday = day == DayOfWeek.SUNDAY;

        if (isSunday && isNightSurcharge) {
            return SurchargeTypeEnum.NIGHT_HOLIDAY;
        } else if (isSunday) {
            return SurchargeTypeEnum.HOLIDAY;
        } else if (isNightSurcharge) {
            return SurchargeTypeEnum.NIGHT;
        } else {
            return SurchargeTypeEnum.DAY;
        }
    }

    private static Surcharge increaseValueOfStepToSurcharge(Surcharge surcharge, LocalDateTime cur, SurchargeTypeEnum type ){
        surcharge.setSurchargeTypeEnum(type);

        if (surcharge.getStart() == null)   {
            surcharge.setStart(cur.minusMinutes(FIRST_THIRTY_MINUTES_WORKED));
        }

        surcharge.setEnd(cur);
        surcharge.increaseValueOfStep();

        return surcharge;
    }

}
