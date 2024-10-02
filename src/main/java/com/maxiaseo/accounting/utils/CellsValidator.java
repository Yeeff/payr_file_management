package com.maxiaseo.accounting.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class CellsValidator {

    public static boolean isValidTimeRange(String timeRange) {
        // Regular expression to match valid time range in 12-hour format, with "am" or "pm"
        String timePattern = "^([1-9]|1[0-2])([ap]m) a ([1-9]|1[0-2])([ap]m)$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher = pattern.matcher(timeRange);

        // Return true if it matches, otherwise false
        return matcher.matches();
    }
}
