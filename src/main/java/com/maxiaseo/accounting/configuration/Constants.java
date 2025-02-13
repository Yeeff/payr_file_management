package com.maxiaseo.accounting.configuration;

import java.util.Arrays;
import java.util.List;

public final class Constants {

    private Constants(){}

    public static final int INDEX_TO_START_TO_WRITE_DATA = 18;

    public static final List<String> ORDERED_METHODS_NAMES_TO_RETRIEVE_OVERTIME_SURCHARGES = Arrays.asList(
            "getTotalSurchargeHoursNight",
            "getTotalSurchargeHoursHoliday",
            "getTotalSurchargeHoursNightHoliday",

            "getTotalOvertimeHoursDay",
            "getTotalOvertimeHoursNight",
            "getTotalOvertimeHoursHoliday",
            "getTotalOvertimeHoursNightHoliday",

            "getTotalOvertimeSurchargeHoursHoliday",
            "getTotalOvertimeSurchargeHoursNightHoliday"
    );

    public static final List<String> HEADERS_TO_NAME_OVERTIME_SURCHARGES = Arrays.asList(
            "HRN",
            "HRF",
            "HRFN",
            "HXD",
            "HXN",
            "HXF",
            "HXFN",
            "HXRF",
            "HXRNF"
    );
}
