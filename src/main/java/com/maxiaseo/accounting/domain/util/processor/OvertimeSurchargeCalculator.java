package com.maxiaseo.accounting.domain.util.processor;

import com.maxiaseo.accounting.domain.model.OvertimeSurcharge;
import com.maxiaseo.accounting.domain.util.ConstantsDomain.OvertimeSurchargeTypeEnum;


import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.maxiaseo.accounting.domain.util.ConstantsDomain.*;

public class OvertimeSurchargeCalculator {

    private OvertimeSurchargeCalculator(){

    }

    public static List<OvertimeSurcharge> getOvertimeSurchargeList(LocalDateTime start, LocalDateTime end) {

        List<OvertimeSurcharge> overtimeSurchargeList = new ArrayList<>();

        OvertimeSurcharge overtimeSurchargeHoliday = new OvertimeSurcharge();
        OvertimeSurcharge overtimeSurchargeHolidayNight = new OvertimeSurcharge();

        LocalDateTime currentTime = start.plusMinutes(STEP_IN_MINUTES);

        while (currentTime.isBefore(end) || currentTime.isEqual(end)) {

            if (getOvertimeType(currentTime) == OvertimeSurchargeTypeEnum.HOLIDAY) {
                overtimeSurchargeHoliday = increaseValueOfStepToSurcharge(overtimeSurchargeHoliday, currentTime, OvertimeSurchargeTypeEnum.HOLIDAY);
            }
            if (getOvertimeType(currentTime) == OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY) {
                overtimeSurchargeHolidayNight = increaseValueOfStepToSurcharge(overtimeSurchargeHolidayNight, currentTime, OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY);
            }

            currentTime = currentTime.plusMinutes(STEP_IN_MINUTES);
        }

        if (overtimeSurchargeHoliday.getQuantityOfMinutes() != 0) overtimeSurchargeList.add(overtimeSurchargeHoliday);
        if (overtimeSurchargeHolidayNight.getQuantityOfMinutes() != 0) overtimeSurchargeList.add(overtimeSurchargeHolidayNight);

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


    private static OvertimeSurcharge increaseValueOfStepToSurcharge(OvertimeSurcharge overtimeSurcharge, LocalDateTime cur, OvertimeSurchargeTypeEnum type ){
        overtimeSurcharge.setOvertimeSurchargeTypeEnum(type);

        if (overtimeSurcharge.getStart() == null)   {
            overtimeSurcharge.setStart(cur.minusMinutes(STEP_IN_MINUTES));
        }

        overtimeSurcharge.setEnd(cur);
        overtimeSurcharge.increaseValueOfStep();

        return overtimeSurcharge;
    }
}
