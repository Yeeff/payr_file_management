package com.maxiaseo.accounting.domain.util.processor;

import com.maxiaseo.accounting.domain.model.Overtime;
import com.maxiaseo.accounting.domain.util.ConstantsDomain.OvertimeTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.maxiaseo.accounting.domain.util.ConstantsDomain.*;

public class OvertimeCalculator {

    private OvertimeCalculator() {
    }


    public static List<Overtime> getOvertimeList(LocalDateTime start, LocalDateTime end) {

        List<Overtime> overtimeList = new ArrayList<>();

        Overtime overtimeDay = new Overtime();
        Overtime overtimeNight =  new Overtime();
        Overtime overtimeHoliday =  new Overtime();
        Overtime overtimeHolidayNight =  new Overtime();

        LocalDateTime currentTime = start.plusMinutes(STEP_IN_MINUTES + 480 );

        while (currentTime.isBefore(end) || currentTime.isEqual(end)) {

            if (getOvertimeType(currentTime) == OvertimeTypeEnum.DAY) {
                overtimeDay = increaseValueOfStepToOvertime(overtimeDay, currentTime, OvertimeTypeEnum.DAY);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.NIGHT) {
                overtimeNight = increaseValueOfStepToOvertime(overtimeNight, currentTime, OvertimeTypeEnum.NIGHT);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.HOLIDAY) {
                overtimeHoliday = increaseValueOfStepToOvertime(overtimeHoliday, currentTime, OvertimeTypeEnum.HOLIDAY);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.NIGHT_HOLIDAY) {
                overtimeHolidayNight = increaseValueOfStepToOvertime(overtimeHolidayNight, currentTime, OvertimeTypeEnum.NIGHT_HOLIDAY);
            }

            currentTime = currentTime.plusMinutes(STEP_IN_MINUTES);
        }

        if (overtimeDay.getQuantityOfMinutes() != 0) overtimeList.add(overtimeDay);
        if (overtimeNight.getQuantityOfMinutes() != 0) overtimeList.add(overtimeNight);
        if (overtimeHoliday.getQuantityOfMinutes() != 0) overtimeList.add(overtimeHoliday);
        if (overtimeHolidayNight.getQuantityOfMinutes() != 0) overtimeList.add(overtimeHolidayNight);

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


    private static Overtime increaseValueOfStepToOvertime(Overtime overtime, LocalDateTime cur, OvertimeTypeEnum type ){
        overtime.setOvertimeTypeEnum(type);

        if (overtime.getStart() == null)   {
            overtime.setStart(cur.minusMinutes(STEP_IN_MINUTES));
        }

        overtime.setEnd(cur);
        overtime.increasValueOfStep();

        return overtime;
    }

}
