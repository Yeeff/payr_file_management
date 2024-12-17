package com.maxiaseo.accounting.domain.util;

import com.maxiaseo.accounting.domain.model.OvertimeSurcharge;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OvertimeSurchargeCalculator {

    private static final LocalTime NIGHT_START = LocalTime.of(21, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    private static final Long FIRST_HOUR_WORKED = 1L;

    public static List<OvertimeSurcharge> getOvertimeSurchargeList(LocalDateTime start, LocalDateTime end) {

        List<OvertimeSurcharge> overtimeSurchargeList = new ArrayList<>();

        OvertimeSurcharge overtimeSurchargeHoliday = OvertimeSurcharge.builder().quantityOfHours(0L).build();
        OvertimeSurcharge overtimeSurchargeHolidayNight = OvertimeSurcharge.builder().quantityOfHours(0L).build();

        LocalDateTime currentTime = start.plusHours(FIRST_HOUR_WORKED );

        while (currentTime.isBefore(end) || currentTime.isEqual(end)) {

            if (getOvertimeType(currentTime) == OvertimeSurchargeTypeEnum.HOLIDAY) {
                overtimeSurchargeHoliday = increaseOneHourSurcharge(overtimeSurchargeHoliday, currentTime, OvertimeSurchargeTypeEnum.HOLIDAY);
            }
            if (getOvertimeType(currentTime) == OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY) {
                overtimeSurchargeHolidayNight =increaseOneHourSurcharge(overtimeSurchargeHolidayNight, currentTime, OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY);
            }

            currentTime = currentTime.plusHours(1);
        }

        if (overtimeSurchargeHoliday.getQuantityOfHours() != 0) overtimeSurchargeList.add(overtimeSurchargeHoliday);
        if (overtimeSurchargeHolidayNight.getQuantityOfHours() != 0) overtimeSurchargeList.add(overtimeSurchargeHolidayNight);

        return overtimeSurchargeList;
    }

    private static OvertimeSurchargeTypeEnum getOvertimeType(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();

        if (time.equals(LocalTime.of(0, 0))) {
            dateTime = dateTime.minusMinutes(1);
        }

        DayOfWeek day = dateTime.getDayOfWeek();
        boolean isNightSurcharge = (time.isAfter(NIGHT_START) || time.isBefore(NIGHT_END) || time.equals(NIGHT_END));
        boolean isSunday = day == DayOfWeek.SUNDAY;

        if (isSunday && isNightSurcharge) {
            return OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY;
        } else {
            return OvertimeSurchargeTypeEnum.HOLIDAY;
        }
    }


    private static OvertimeSurcharge increaseOneHourSurcharge(OvertimeSurcharge overtimeSurcharge, LocalDateTime cur, OvertimeSurchargeTypeEnum type ){
        overtimeSurcharge.setOvertimeSurchargeTypeEnum(type);

        if (overtimeSurcharge.getStart() == null)   {
            overtimeSurcharge.setStart(cur);
        }

        overtimeSurcharge.setEnd(cur);
        overtimeSurcharge.increaseOneHour();

        return overtimeSurcharge;
    }
}
