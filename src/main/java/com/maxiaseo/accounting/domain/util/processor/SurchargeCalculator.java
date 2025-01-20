package com.maxiaseo.accounting.domain.util.processor;

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

        LocalDateTime current = start.plusHours(FIRST_HOUR_WORKED);

        while (current.isBefore(end) || current.isEqual(end)) {

            if (getSurchargeType(current) == SurchargeTypeEnum.NIGHT) {
                surchargeNight =increaseOneHourSurcharge(surchargeNight, current, SurchargeTypeEnum.NIGHT);
            }
            if (getSurchargeType(current) == SurchargeTypeEnum.HOLIDAY) {
                surchargeHoliday =increaseOneHourSurcharge(surchargeHoliday, current, SurchargeTypeEnum.HOLIDAY);
            }
            if (getSurchargeType(current) == SurchargeTypeEnum.NIGHT_HOLIDAY) {
                surchargeHolidayNight =increaseOneHourSurcharge(surchargeHolidayNight, current, SurchargeTypeEnum.NIGHT_HOLIDAY);
            }

            LocalDateTime maxTimeToCheckSurcharges = start.plusHours(MAX_HOURS_BY_DAY );
            if(maxTimeToCheckSurcharges.isEqual(current)) break;

            current = current.plusHours(1);
        }

        if (surchargeNight.getQuantityOfHours() != 0) surchargesList.add(surchargeNight);
        if (surchargeHoliday.getQuantityOfHours() != 0) surchargesList.add(surchargeHoliday);
        if (surchargeHolidayNight.getQuantityOfHours() != 0) surchargesList.add(surchargeHolidayNight);

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

    private static Surcharge increaseOneHourSurcharge(Surcharge surcharge, LocalDateTime cur, SurchargeTypeEnum type ){
        surcharge.setSurchargeTypeEnum(type);

        if (surcharge.getStart() == null)   {
            surcharge.setStart(cur.minusHours(FIRST_HOUR_WORKED));
        }

        surcharge.setEnd(cur);
        surcharge.increaseOneHour();

        return surcharge;
    }

}
