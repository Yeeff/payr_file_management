package com.maxiaseo.accounting.domain.util;

import com.maxiaseo.accounting.domain.model.Surcharge;
import com.maxiaseo.accounting.domain.util.ConstantsDomain.SurchargeTypeEnum;
import com.maxiaseo.accounting.domain.util.processor.SurchargeCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SurchargeCalculatorTest {

    @Test
    public void testNightSurchargeWithOrdinaryWaysBefore() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 20, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 21, 2, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge nightSurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(1, result.size());

        assertEquals(SurchargeTypeEnum.NIGHT, nightSurcharge.getSurchargeTypeEnum());
        assertEquals(300L, nightSurcharge.getQuantityOfMinutes());
    }

    @Test
    public void testNightSurchargeWithOrdinaryWaysBeforeHalfHours() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 20, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 21, 1, 30);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge nightSurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(1, result.size());

        assertEquals(SurchargeTypeEnum.NIGHT, nightSurcharge.getSurchargeTypeEnum());
        assertEquals(270L, nightSurcharge.getQuantityOfMinutes());
    }

    @Test
    public void testNightSurchargeWithOrdinaryWaysAfter() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 20, 2, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 20, 9, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge nightSurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(1, result.size());

        assertEquals(SurchargeTypeEnum.NIGHT, nightSurcharge.getSurchargeTypeEnum());
        assertEquals(240L, nightSurcharge.getQuantityOfMinutes());
    }

    @Test
    public void testHolidaySurchargeWithOrdinaryWaysBefore() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 22, 4, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 10, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge holidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(2, result.size());

        assertEquals(SurchargeTypeEnum.HOLIDAY, holidaySurcharge.getSurchargeTypeEnum());
        assertEquals(240L, holidaySurcharge.getQuantityOfMinutes());  // 8 hours of holiday overtime
    }

    @Test
    public void testHolidaySurchargeWithOrdinaryWaysAfter() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 22, 16, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 23, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge holidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(2, result.size());

        assertEquals(SurchargeTypeEnum.HOLIDAY, holidaySurcharge.getSurchargeTypeEnum());
        assertEquals(300L, holidaySurcharge.getQuantityOfMinutes());  // 8 hours of holiday overtime
    }

    @Test
    public void testNightHolidaySurchargeWithOtherTypeOfDaysBefore() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 22, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 23, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge nightHolidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(2, result.size());

        assertEquals(SurchargeTypeEnum.NIGHT_HOLIDAY, nightHolidaySurcharge.getSurchargeTypeEnum());
        assertEquals(120L, nightHolidaySurcharge.getQuantityOfMinutes());  // 8 hours of night holiday overtime
    }

    @Test
    public void testNightHolidaySurchargeWithOtherTypeOfDaysAfter() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 22, 3, 0);  // Monday holiday, 9 PM
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 10, 0);     // 5 AM next day

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge nightHolidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(2, result.size());

        assertEquals(SurchargeTypeEnum.NIGHT_HOLIDAY, nightHolidaySurcharge.getSurchargeTypeEnum());
        assertEquals(180L, nightHolidaySurcharge.getQuantityOfMinutes());  // 8 hours of night holiday overtime
    }

    @Test
    public void testMixedSurcharges() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 15, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 16, 2, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge nightSurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Surcharge holidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday surcharge not found"));

        Surcharge nightHolidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night-Holiday surcharge not found"));

        Long nightQuantityHours = nightSurcharge.getQuantityOfMinutes();
        Long holidayQuantityHours = holidaySurcharge.getQuantityOfMinutes();
        Long nightHolidayQuantityHours = nightHolidaySurcharge.getQuantityOfMinutes();

        // Verify Night Surcharge
        assertEquals(SurchargeTypeEnum.NIGHT, nightSurcharge.getSurchargeTypeEnum());
        assertEquals(120L, nightQuantityHours);  // 2 hours (Sunday night)

        // Verify Holiday Surcharge
        assertEquals(SurchargeTypeEnum.HOLIDAY, holidaySurcharge.getSurchargeTypeEnum());
        assertEquals(180L, holidayQuantityHours);  // 10 hours (Monday day)

        // Verify Night-Holiday Surcharge
        assertEquals(SurchargeTypeEnum.NIGHT_HOLIDAY, nightHolidaySurcharge.getSurchargeTypeEnum());
        assertEquals(180L, nightHolidayQuantityHours);  // 2 hours (Monday night)

    }

    @Test
    public void testMixedSurcharges2() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 15, 20, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 16, 4, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge nightSurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Surcharge holidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday surcharge not found"));

        Surcharge nightHolidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night-Holiday surcharge not found"));

        Long nightQuantityHours = nightSurcharge.getQuantityOfMinutes();
        Long holidayQuantityHours = holidaySurcharge.getQuantityOfMinutes();
        Long nightHolidayQuantityHours = nightHolidaySurcharge.getQuantityOfMinutes();

        assertEquals(SurchargeTypeEnum.NIGHT, nightSurcharge.getSurchargeTypeEnum());
        assertEquals(240L, nightQuantityHours);  // 2 hours (Sunday night)

        assertEquals(SurchargeTypeEnum.HOLIDAY, holidaySurcharge.getSurchargeTypeEnum());
        assertEquals(60L, holidayQuantityHours);  // 10 hours (Monday day)

        assertEquals(SurchargeTypeEnum.NIGHT_HOLIDAY, nightHolidaySurcharge.getSurchargeTypeEnum());
        assertEquals(180L, nightHolidayQuantityHours);  // 2 hours (Monday night)

    }

    @Test
    public void testNightSurchargeBeingMaxHours() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 20, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 21, 16, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge nightSurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(1, result.size());

        assertEquals(SurchargeTypeEnum.NIGHT, nightSurcharge.getSurchargeTypeEnum());
        assertEquals(300L, nightSurcharge.getQuantityOfMinutes());
    }

    @Test
    public void testHolidaySurchargeBeingMaxHours() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 22, 4, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 23, 0);

        List<Surcharge> result = SurchargeCalculator.getSurchargeList(start, end);

        Surcharge holidaySurcharge = result.stream()
                .filter(s -> s.getSurchargeTypeEnum() == SurchargeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        assertEquals(2, result.size());

        assertEquals(SurchargeTypeEnum.HOLIDAY, holidaySurcharge.getSurchargeTypeEnum());
        assertEquals(360L, holidaySurcharge.getQuantityOfMinutes());  // 8 hours of holiday overtime
    }


}