package com.maxiaseo.accounting.services;

import com.maxiaseo.accounting.domain.Employee;
import com.maxiaseo.accounting.domain.Overtime;
import com.maxiaseo.accounting.domain.OvertimeSurcharge;
import com.maxiaseo.accounting.domain.Surcharge;
import com.maxiaseo.accounting.utils.CellsValidator;
import com.maxiaseo.accounting.utils.OvertimeCalculator;
import com.maxiaseo.accounting.utils.OvertimeSurchargeCalculator;
import com.maxiaseo.accounting.utils.SurchargeCalculator;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

@Service
public class PayrollServices {

    Long hoursWorkedPerWeek;
    private static final Long MAX_HOURS_BY_DAY = 8L;
    private static final Long MAX_HOURS_BY_WEEK = 48L;

    public PayrollServices() {
        this.hoursWorkedPerWeek = 0L;
    }

    public List<Employee> handleFileUpload( File tempFile, Integer year, Integer month, Integer initDay) throws IOException {
        Workbook workbook;

        LocalDate initDateOfFortnight = LocalDate.of(year,month, initDay);
        LocalDate currentDate;

        //---------------------------------------------------
        if (tempFile != null && tempFile.exists()) {
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                workbook = WorkbookFactory.create(fis);
            } catch (IOException e) {
                throw new IOException("Failed to create a Workbook from the file", e);
            }
        } else {
            throw new IOException("Temporary file does not exist.");
        }
        //---------------------------------------------------

        Sheet sheet = workbook.getSheetAt(0);

        List<Employee> employees = new ArrayList<>();

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {

            Employee employee = Employee.builder()
                    .surcharges(new ArrayList<>())
                    .overtimes(new ArrayList<>())
                    .overtimeSurcharges(new ArrayList<>())
                    .build();
            currentDate = initDateOfFortnight;

            Row row = sheet.getRow(i);
            if (row != null) {

                try{
                    employee.setId( Long.valueOf(getCellValueAsString(row.getCell(1))));
                    employee.setName(getCellValueAsString(row.getCell(2)));
                }catch (Exception e){
                    break;
                }

                for (int j = 3; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        String cellValue = getCellValueAsString(cell);

                        int x=1;
                        if(i==36){
                            x=8;
                        }

                        if (CellsValidator.isValidTimeRange(cellValue)){
                            employee = processSchedule( cellValue, currentDate, employee);
                        }
                    }

                    if(currentDate.getDayOfWeek() == DayOfWeek.SUNDAY)
                        hoursWorkedPerWeek = 0L;

                    currentDate =currentDate.plusDays(1);
                    if(initDateOfFortnight.plusMonths(1).getDayOfMonth() == currentDate.getDayOfMonth()){
                        break;
                    }
                }
            }
            if(!employee.getSurcharges().isEmpty()) employees.add(employee);

            //---------------------------------------------------
            Cell totalHoldaytSurchargeCell = row.createCell(24);
            totalHoldaytSurchargeCell.setCellValue(employee.getTotalHolidaySurchargeHours());

            Cell totalNightSurchargeCell = row.createCell(27);
            totalNightSurchargeCell.setCellValue(employee.getTotalNightSurchargeHours());

            Cell totalNighHolidaytSurchargeCell = row.createCell(28);
            totalNighHolidaytSurchargeCell.setCellValue(employee.getTotalNightHolidaySurchargeHours());


            Cell totalDayOvertimeCell = row.createCell(20);
            totalDayOvertimeCell.setCellValue(employee.getTotalDayOvertimeHours());

            Cell totalHoldaytOvertimeCell = row.createCell(22);
            totalHoldaytOvertimeCell.setCellValue(employee.getTotalHolidayOvertimeHours());

            Cell totalNightOvertimeCell = row.createCell(21);
            totalNightOvertimeCell.setCellValue(employee.getTotalNightOvertimeHours());

            Cell totalNighHolidaytOvertimeCell = row.createCell(23);
            totalNighHolidaytOvertimeCell.setCellValue(employee.getTotalNightHolidayOvertimeHours());


            Cell totalHolidayOvertimeSurchargeCell = row.createCell(25);
            totalHolidayOvertimeSurchargeCell.setCellValue(employee.getTotalHolidayOvertimeSurchargeHours());

            Cell totalNighHolidaytOvertimeSurchargeCell = row.createCell(26);
            totalNighHolidaytOvertimeSurchargeCell.setCellValue(employee.getTotalNightHolidayOvertimeSurchargeHours());
            //---------------------------------------------------

        }

        //---------------------------------------------------
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            workbook.write(fos);
        }
        workbook.close();
        //---------------------------------------------------

        return employees;
    }

    private Employee processSchedule( String schedule, LocalDate date,Employee employee) {

        String[] times = schedule.split(" a");
        LocalDateTime startTime = parseTime(times[0].trim(), date);
        LocalDateTime endTime = parseTime(times[1].trim(), date);

        if(endTime.isBefore(startTime))
            endTime = endTime.plusDays(1);

        if(hoursWorkedPerWeek >= MAX_HOURS_BY_WEEK && startTime.getDayOfWeek() == DayOfWeek.SUNDAY ){
            for ( OvertimeSurcharge overtimeSurcharge : OvertimeSurchargeCalculator.getOvertimeSurchargeList(startTime, endTime) ){
                if(overtimeSurcharge.getQuantityOfHours() != 0){
                    employee.addNewOverTimeSurcharge(overtimeSurcharge);
                }
            }
        }else{
            for ( Surcharge surcharge : SurchargeCalculator.getSurchargeList(startTime, endTime) ){
                if(surcharge.getQuantityOfHours() != 0){
                    employee.addNewSurcharge(surcharge);
                }
            }

            for ( Overtime overtime : OvertimeCalculator.getOvertimeList(startTime, endTime) ){
                if(overtime.getQuantityOfHours() != 0){
                    employee.addNewOverTime(overtime);
                }
            }
        }

        Long hoursWorkedPerDay = Duration.between(startTime,endTime).toHours();

        if (hoursWorkedPerDay > MAX_HOURS_BY_DAY)
            hoursWorkedPerWeek += MAX_HOURS_BY_DAY;
        else
            hoursWorkedPerWeek += hoursWorkedPerDay ;

        return employee;
    }

    private LocalDateTime parseTime(String timeString, LocalDate date) {

        DateTimeFormatter format = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("ha")
                .toFormatter(Locale.ENGLISH);
        LocalTime time = LocalTime.parse(timeString, format);

        return LocalDateTime.of(date, time);

    }

    public String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:

                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Use BigDecimal to avoid scientific notation for large numbers
                    BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue());
                    return bd.setScale(0, RoundingMode.HALF_UP).toPlainString(); // Return the plain string representation
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    public File saveTemporaryFile(MultipartFile file) throws IOException {
        // Create a unique temp file in the default temporary directory
        File tempFile = File.createTempFile("uploaded-", file.getOriginalFilename());

        // Write the uploaded file content to the temp file
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;  // Return the temp file reference for later use
    }

    // Method to retrieve the processed file (if needed)
    public File getProcessedFile(File tempFile) {
        return tempFile.exists() ? tempFile : null;
    }

    // Delete the temporary file after it's no longer needed
    public void deleteTemporaryFile(File tempFile) {
        if (tempFile.exists()) {
            tempFile.delete();  // Remove the file from the temporary location
        }
    }



}
