package com.maxiaseo.accounting.utils;

import com.maxiaseo.accounting.domain.model.Surcharge;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SurchargeCalculator {

    // Constants for defining night hours (e.g., 10 PM to 6 AM)
    private static final LocalTime NIGHT_START = LocalTime.of(21, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    private static final Long MAX_HOURS_BY_DAY = 8L;

    private static final Long FIRST_HOUR_WORKED = 1L;

    public static List<Surcharge> getSurchargeList(LocalDateTime start, LocalDateTime end) {

        List<Surcharge> surchargesList = new ArrayList<>();

        Surcharge surchargeNight = Surcharge.builder().quantityOfHours(0L).build();
        Surcharge surchargeHoliday = Surcharge.builder().quantityOfHours(0L).build();
        Surcharge surchargeHolidayNight = Surcharge.builder().quantityOfHours(0L).build();

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
            surcharge.setStart(cur);
        }

        surcharge.setEnd(cur);
        surcharge.increaseOneHour();

        return surcharge;
    }

}
