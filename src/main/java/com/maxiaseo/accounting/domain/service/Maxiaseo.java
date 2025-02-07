package com.maxiaseo.accounting.domain.service;

import com.maxiaseo.accounting.domain.util.TimeRange;

import java.time.Duration;

public class Maxiaseo {
    public TimeRange validateLunchHour(TimeRange timeRange){
        Long hoursWorkedPerDay = Duration.between(
                timeRange.getStartTime(), timeRange.getEndTime() ).toHours();

        if( hoursWorkedPerDay == 9 ){
            timeRange.setEndTime(timeRange.getEndTime().minusHours(1));
        }

        return timeRange;
    }
}
