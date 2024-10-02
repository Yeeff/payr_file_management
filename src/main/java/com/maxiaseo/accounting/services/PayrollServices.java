package com.maxiaseo.accounting.services;

import com.maxiaseo.accounting.domain.Employee;
import com.maxiaseo.accounting.domain.Surcharge;
import com.maxiaseo.accounting.utils.CellsValidator;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

@Service
public class PayrollServices {

    public List<Employee> handleFileUpload( File tempFile) throws IOException {
        Workbook workbook;

        if (tempFile != null && tempFile.exists()) {
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                workbook = WorkbookFactory.create(fis);
            } catch (IOException e) {
                throw new IOException("Failed to create a Workbook from the file", e);
            }
        } else {
            throw new IOException("Temporary file does not exist.");
        }

        Sheet sheet = workbook.getSheetAt(0);

        List<Employee> employees = new ArrayList<>();

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {

            Employee employee = Employee.builder().surcharges(new ArrayList<>()).build();

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

                        if (CellsValidator.isValidTimeRange(cellValue)){
                            employee = processSchedule( cellValue, j - 1, employee);
                        }else{
                            //throw new RuntimeException("Unrecognized values of cell at row: " + i + " colum: " +j+ " Value: "+cellValue);
                            continue;
                        }
                    }
                }
            }
            employees.add(employee);

            Cell totalHoldaytSurchargeCell = row.createCell(24);
            totalHoldaytSurchargeCell.setCellValue(employee.getTotalHolidaySurchargeHours());

            Cell totalNightSurchargeCell = row.createCell(27);
            totalNightSurchargeCell.setCellValue(employee.getTotalNightSurchargeHours());

            Cell totalNighHolidaytSurchargeCell = row.createCell(28);
            totalNighHolidaytSurchargeCell.setCellValue(employee.getTotalNightHolidaySurchargeHours());

        }
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            workbook.write(fos);
        }
        workbook.close();

        return employees;
    }

    private Employee processSchedule( String schedule, Integer day,Employee employee) {

        String[] times = schedule.split(" a");
        LocalDateTime startTime = parseTime(times[0].trim(), day);
        LocalDateTime endTime = parseTime(times[1].trim(), day);


        for ( Surcharge surcharge : SurchargeCalculator.getSurchargeList(startTime, endTime) ){
            employee.addNewSurcharge(surcharge);
        }

        return employee;
    }

    private LocalDateTime parseTime(String timeString, Integer day) {

        DateTimeFormatter format = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("ha")
                .toFormatter(Locale.ENGLISH);
        LocalTime time = LocalTime.parse(timeString, format);

        return LocalDateTime.of(LocalDate.of(2024,9,day), time);

    }

    public String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
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

    ///////////////////////////////////////
    // Save the file temporarily on the disk
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
