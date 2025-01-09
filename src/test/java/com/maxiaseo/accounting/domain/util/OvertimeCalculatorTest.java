package com.maxiaseo.accounting.domain.util;

import com.maxiaseo.accounting.domain.model.Overtime;
import com.maxiaseo.accounting.domain.util.ConstantsDomain.OvertimeTypeEnum;
import com.maxiaseo.accounting.domain.util.processor.OvertimeCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OvertimeCalculatorTest {

    @Test
    public void testOneDayOvertime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 20, 7, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 20, 16, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.DAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Day overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.DAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(1L, dayOvertime.getQuantityOfHours());
    }

    @Test
    public void testMOreThanOneDayOvertime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 20, 7, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 20, 18, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.DAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Day overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.DAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(3L, dayOvertime.getQuantityOfHours());
    }

    @Test
    public void testOneNightOverTime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 19, 14, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 19, 23, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.NIGHT, dayOvertime.getOvertimeTypeEnum());
        assertEquals(1L, dayOvertime.getQuantityOfHours());
    }

    @Test
    public void testMoreThanOneNightOverTime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 19, 14, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 20, 2, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.NIGHT, dayOvertime.getOvertimeTypeEnum());
        assertEquals(4L, dayOvertime.getQuantityOfHours());
    }

    @Test
    public void testOneHolidayOverTime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 22, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 17, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.HOLIDAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(1L, dayOvertime.getQuantityOfHours());
    }

    @Test
    public void testMoreThanOneHolidayOverTime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 22, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 19, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.HOLIDAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(3L, dayOvertime.getQuantityOfHours());
    }

    @Test
    public void testOneHolidayNightOverTime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 21, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 3, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night Holiday overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.NIGHT_HOLIDAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(1L, dayOvertime.getQuantityOfHours());
    }

    @Test
    public void testMoreThanOneHolidayNightOverTime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 21, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 5, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night Holiday overtime not found"));

        assertEquals(1, result.size());

        assertEquals(OvertimeTypeEnum.NIGHT_HOLIDAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(3L, dayOvertime.getQuantityOfHours());
    }

    @Test
    public void testMixedOvertime() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 21, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 23, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.DAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Overtime nightOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Overtime holidayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday surcharge not found"));

        Overtime nightHolidayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night-Holiday surcharge not found"));

        Long dayQuantityHours = dayOvertime.getQuantityOfHours();
        Long nightQuantityHours = nightOvertime.getQuantityOfHours();
        Long holidayQuantityHours = holidayOvertime.getQuantityOfHours();
        Long nightHolidayQuantityHours = nightHolidayOvertime.getQuantityOfHours();

        assertEquals(OvertimeTypeEnum.DAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(5L, dayQuantityHours);

        assertEquals(OvertimeTypeEnum.NIGHT, nightOvertime.getOvertimeTypeEnum());
        assertEquals(3L, nightQuantityHours);

        // Verify Holiday Surcharge
        assertEquals(OvertimeTypeEnum.HOLIDAY, holidayOvertime.getOvertimeTypeEnum());
        assertEquals(15L, holidayQuantityHours);  // 10 hours (Monday day)

        // Verify Night-Holiday Surcharge
        assertEquals(OvertimeTypeEnum.NIGHT_HOLIDAY, nightHolidayOvertime.getOvertimeTypeEnum());
        assertEquals(8L, nightHolidayQuantityHours);  // 2 hours (Monday night)
    }

    @Test
    public void testMixedOvertime2() {
        LocalDateTime start = LocalDateTime.of(2024, 9, 21, 6, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 22, 22, 0);

        List<Overtime> result = OvertimeCalculator.getOvertimeList(start, end);

        Overtime dayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.DAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Overtime nightOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night surcharge not found"));

        Overtime holidayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Holiday surcharge not found"));

        Overtime nightHolidayOvertime = result.stream()
                .filter(s -> s.getOvertimeTypeEnum() == OvertimeTypeEnum.NIGHT_HOLIDAY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Night-Holiday surcharge not found"));

        Long dayQuantityHours = dayOvertime.getQuantityOfHours();
        Long nightQuantityHours = nightOvertime.getQuantityOfHours();
        Long holidayQuantityHours = holidayOvertime.getQuantityOfHours();
        Long nightHolidayQuantityHours = nightHolidayOvertime.getQuantityOfHours();

        assertEquals(OvertimeTypeEnum.DAY, dayOvertime.getOvertimeTypeEnum());
        assertEquals(7L, dayQuantityHours);

        assertEquals(OvertimeTypeEnum.NIGHT, nightOvertime.getOvertimeTypeEnum());
        assertEquals(3L, nightQuantityHours);

        // Verify Holiday Surcharge
        assertEquals(OvertimeTypeEnum.HOLIDAY, holidayOvertime.getOvertimeTypeEnum());
        assertEquals(15L, holidayQuantityHours);  // 10 hours (Monday day)

        // Verify Night-Holiday Surcharge
        assertEquals(OvertimeTypeEnum.NIGHT_HOLIDAY, nightHolidayOvertime.getOvertimeTypeEnum());
        assertEquals(7L, nightHolidayQuantityHours);  // 2 hours (Monday night)
    }

}