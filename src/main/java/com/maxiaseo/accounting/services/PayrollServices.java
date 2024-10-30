package com.maxiaseo.accounting.services;

import com.maxiaseo.accounting.domain.exception.IncorrectFormatExcelValuesException;
import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.model.Overtime;
import com.maxiaseo.accounting.domain.model.OvertimeSurcharge;
import com.maxiaseo.accounting.domain.model.Surcharge;
import com.maxiaseo.accounting.utils.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.maxiaseo.accounting.configuration.Constants.*;

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

        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= sheet.getLastRowNum(); i++) {

            Employee employee = Employee.builder()
                    .surcharges(new ArrayList<>())
                    .overtimes(new ArrayList<>())
                    .overtimeSurcharges(new ArrayList<>())
                    .build();
            currentDate = initDateOfFortnight;

            Row row = sheet.getRow(i);
            if (row != null) {

                try{
                    employee.setId( Long.valueOf(CellsValidator.getCellValueAsString(row.getCell(EMPLOYEE_DOCUMENT_ID_INDEX))));
                    employee.setName(CellsValidator.getCellValueAsString(row.getCell(EMPLOYEE_NAME_INDEX)));
                }catch (Exception e){
                    break;
                }

                for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        String cellValue = CellsValidator.getCellValueAsString(cell);

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

    public  File saveFile(MultipartFile file) throws IOException {

        Workbook workbook;
        Sheet sheet = null;
        Map<String, String> errorsMap = new HashMap<>();

        Boolean blankLineFound = false;

        LocalDate initDateOfFortnight = LocalDate.of(2024,9, 1);
        LocalDate currentDate;

        if (!file.getOriginalFilename().endsWith(".xls") && !file.getOriginalFilename().endsWith(".xlsx")) {
            throw new IllegalArgumentException("The specified file is not an Excel file");
        }

        try {
            InputStream fis = file.getInputStream();

            workbook = WorkbookFactory.create(fis);

            sheet = workbook.getSheetAt(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= sheet.getLastRowNum(); i++) {

            currentDate = initDateOfFortnight;

            Row row = sheet.getRow(i);
            if (row != null) {

                if(CellsValidator.getCellValueAsString(row.getCell(0)) == "")
                    if(CellsValidator.isAEmptyLine(row)){
                        blankLineFound = true;
                        break;
                    }

                String employeeName = CellsValidator.getCellValueAsString(row.getCell(EMPLOYEE_NAME_INDEX));
                if(employeeName == ""){
                    errorsMap.put(CellsValidator.getExcelCoordinate(i,EMPLOYEE_NAME_INDEX)
                            , String.format("El campo nombre no puede estar vacio."));
                }

                String employeeDocumentId = CellsValidator.getCellValueAsString(row.getCell(EMPLOYEE_DOCUMENT_ID_INDEX));
                if(!CellsValidator.isANumber(employeeDocumentId)){
                    errorsMap.put( CellsValidator.getExcelCoordinate(i,EMPLOYEE_DOCUMENT_ID_INDEX),
                            String.format("El valor '%s' no es válido como numero de identificacion del empleado.",
                            employeeDocumentId
                    ));
                }

                for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < row.getLastCellNum(); j++) {

                    Cell cell = row.getCell(j);
                    String cellValue = CellsValidator.getCellValueAsString(cell);

                    if(initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_FIRST_FORTNIGHT){
                        if(currentDate.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT){
                            if(cellValue != ""){
                                errorsMap.put(i + "," + j, String.format("El ultimo dia la quincena debe ser 15 pero después de esa columana se encontro el valor: %s",
                                        cellValue
                                ));
                            }
                            break;
                        }

                    } else if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT) {

                        Integer nextMoth = initDateOfFortnight.plusMonths(1).getDayOfMonth();
                        if(currentDate.getDayOfMonth() == nextMoth ){

                            if(cellValue != ""){
                                errorsMap.put(i + "," + j, String.format("El ultimo dia de %s es %s pero después de esa columana se encontro el valor: %s",
                                        initDateOfFortnight.getMonth(),
                                        initDateOfFortnight.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(),
                                        cellValue
                                ));
                            }
                            break;
                        }
                    }


                    if (!CellsValidator.isValidTimeRange(cellValue) && !CellsValidator.isValidAbsenceReasons(cellValue)) {
                        errorsMap.put(i + "," + j, String.format("-> " +cellValue + " <- no, es un valor valido",
                                initDateOfFortnight.getMonth(),
                                initDateOfFortnight.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(),
                                cellValue
                        ));
                    }

                    currentDate =currentDate.plusDays(1);
                }
            }

            if(blankLineFound)
                break;

        }
        if( ! errorsMap.isEmpty())
            throw new IncorrectFormatExcelValuesException(errorsMap.toString());

        return FileAdministrator.saveTemporaryFile(file);

    }

    //--
    public File getProcessedFile(File tempFile) {
        return tempFile.exists() ? tempFile : null;
    }
    //--
    public void deleteTemporaryFile(File tempFile) {
        if (tempFile.exists()) {
            tempFile.delete();  // Remove the file from the temporary location
        }
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

}
