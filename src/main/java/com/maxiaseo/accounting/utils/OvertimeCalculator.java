package com.maxiaseo.accounting.utils;

import com.maxiaseo.accounting.domain.Overtime;
import com.maxiaseo.accounting.domain.Surcharge;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OvertimeCalculator {

    private static final LocalTime NIGHT_START = LocalTime.of(21, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    private static final Long MAX_HOURS_BY_DAY = 8L;

    private static final Long FIRST_HOUR_WORKED = 1l;

    public static List<Overtime> getOvertimeList(LocalDateTime start, LocalDateTime end) {

        List<Overtime> overtimeList = new ArrayList<>();

        Overtime overtimeDay = Overtime.builder().quantityOfHours(0L).build();
        Overtime overtimeNight = Overtime.builder().quantityOfHours(0L).build();
        Overtime overtimeHoliday = Overtime.builder().quantityOfHours(0L).build();
        Overtime overtimeHolidayNight = Overtime.builder().quantityOfHours(0L).build();

        LocalDateTime currentTime = start.plusHours(FIRST_HOUR_WORKED + MAX_HOURS_BY_DAY );

        while (currentTime.isBefore(end) || currentTime.isEqual(end)) {

            if (getOvertimeType(currentTime) == OvertimeTypeEnum.DAY) {
                overtimeDay =increaseOneHourSurcharge(overtimeDay, currentTime, OvertimeTypeEnum.DAY);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.NIGHT) {
                overtimeNight =increaseOneHourSurcharge(overtimeNight, currentTime, OvertimeTypeEnum.NIGHT);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.HOLIDAY) {
                overtimeHoliday =increaseOneHourSurcharge(overtimeHoliday, currentTime, OvertimeTypeEnum.HOLIDAY);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.NIGHT_HOLIDAY) {
                overtimeHolidayNight =increaseOneHourSurcharge(overtimeHolidayNight, currentTime, OvertimeTypeEnum.NIGHT_HOLIDAY);
            }

            currentTime = currentTime.plusHours(1);
        }

        if (overtimeDay.getQuantityOfHours() != 0) overtimeList.add(overtimeDay);
        if (overtimeNight.getQuantityOfHours() != 0) overtimeList.add(overtimeNight);
        if (overtimeHoliday.getQuantityOfHours() != 0) overtimeList.add(overtimeHoliday);
        if (overtimeHolidayNight.getQuantityOfHours() != 0) overtimeList.add(overtimeHolidayNight);

        return overtimeList;
    }

    private static OvertimeTypeEnum getOvertimeType(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();

        if (time.equals(LocalTime.of(0, 0))) {
            dateTime = dateTime.minusMinutes(1);
        }

        DayOfWeek day = dateTime.getDayOfWeek();
        boolean isNightSurcharge = (time.isAfter(NIGHT_START) || time.isBefore(NIGHT_END) || time.equals(NIGHT_END));
        boolean isSunday = day == DayOfWeek.SUNDAY;

        if (isSunday && isNightSurcharge) {
            return OvertimeTypeEnum.NIGHT_HOLIDAY;
        } else if (isSunday) {
            return OvertimeTypeEnum.HOLIDAY;
        } else if (isNightSurcharge) {
            return OvertimeTypeEnum.NIGHT;
        } else {
            return OvertimeTypeEnum.DAY;
        }
    }


    private static Overtime increaseOneHourSurcharge(Overtime overtime, LocalDateTime cur, OvertimeTypeEnum type ){
        overtime.setOvertimeTypeEnum(type);

        if (overtime.getStart() == null)   {
            overtime.setStart(cur);
        }

        overtime.setEnd(cur);
        overtime.increaseOneHour();

        return overtime;
    }

}
