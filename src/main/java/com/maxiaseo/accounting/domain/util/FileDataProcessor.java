package com.maxiaseo.accounting.domain.util;

import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.model.Overtime;
import com.maxiaseo.accounting.domain.model.OvertimeSurcharge;
import com.maxiaseo.accounting.domain.model.Surcharge;
import com.maxiaseo.accounting.utils.OvertimeCalculator;
import com.maxiaseo.accounting.utils.OvertimeSurchargeCalculator;
import com.maxiaseo.accounting.utils.SurchargeCalculator;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.maxiaseo.accounting.configuration.Constants.*;
import static com.maxiaseo.accounting.configuration.Constants.FIRST_DAY_OF_SECOND_FORTNIGHT;

public class FileDataProcessor {

    private static final Long MAX_HOURS_BY_DAY = 8L;
    private static final Long MAX_HOURS_BY_WEEK = 48L;

    Long hoursWorkedPerWeek ;

    public FileDataProcessor() {
        this.hoursWorkedPerWeek = 0L;
    }

    public  List<Employee> extractEmployeeData(List<List<String>> listOfListData, Integer year, Integer month, Integer initDay){


        LocalDate initDateOfFortnight = LocalDate.of(year,month, initDay);
        LocalDate currentDate;

        List<Employee> employees = new ArrayList<>();

        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= listOfListData.size(); i++) {

            Employee employee = Employee.builder()
                    .surcharges(new ArrayList<>())
                    .overtimes(new ArrayList<>())
                    .overtimeSurcharges(new ArrayList<>())
                    .build();

            currentDate = initDateOfFortnight;

            List<String> dataRowList = listOfListData.get(i);
            if (dataRowList.size() != 0) {

                try{
                    employee.setId( Long.valueOf(dataRowList.get(EMPLOYEE_DOCUMENT_ID_INDEX)));
                    employee.setName(dataRowList.get(EMPLOYEE_NAME_INDEX));
                }catch (Exception e){
                    break;
                }

                for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < dataRowList.size(); j++) {

                    if (dataRowList.get(j) != null) {
                        String cellValue = dataRowList.get(j);

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

        }
        return employees;
    }

    public  Map<String, String> getErrorsFormat(Integer year, Integer month, Integer day,
                                                      List<List<String >> listOfListExcelData
    ){

        Map<String, String> errorsMap = new HashMap<>();

        Boolean blankLineFound = false;

        LocalDate initDateOfFortnight = LocalDate.of(year,month, day);
        LocalDate currentDate;


        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= listOfListExcelData.size(); i++) {

            currentDate = initDateOfFortnight;

            List<String> dataRowList = listOfListExcelData.get(i);
            if (dataRowList.size() != 0) {

                if(dataRowList.get(0) == "")
                    if(CellsValidator.isAEmptyLine(dataRowList)){
                        blankLineFound = true;
                        break;
                    }

                String employeeName = dataRowList.get(EMPLOYEE_NAME_INDEX);
                if(employeeName == ""){
                    errorsMap.put(CellsValidator.getExcelCoordinate(i,EMPLOYEE_NAME_INDEX)
                            , String.format("El campo nombre no puede estar vacio."));
                }

                String employeeDocumentId =  dataRowList.get(EMPLOYEE_DOCUMENT_ID_INDEX) ;
                if(!CellsValidator.isANumber(employeeDocumentId)){
                    errorsMap.put( CellsValidator.getExcelCoordinate(i, EMPLOYEE_DOCUMENT_ID_INDEX),
                            String.format("El valor '%s' no es válido como numero de identificacion del empleado.",
                                    employeeDocumentId
                            ));
                }

                for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < dataRowList.size(); j++) {

                    String cellValue = dataRowList.get(j);

                    if(initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_FIRST_FORTNIGHT){
                        if(currentDate.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT){
                            if(cellValue != ""){
                                errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                                        String.format("El ultimo dia la quincena debe ser 15 pero después de esa columana se encontro el valor: %s",
                                                cellValue
                                        ));
                            }
                            break;
                        }

                    } else if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT) {

                        Integer nextMoth = initDateOfFortnight.plusMonths(1).getDayOfMonth();
                        if(currentDate.getDayOfMonth() == nextMoth ){

                            if(cellValue != ""){
                                errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                                        String.format("El ultimo dia de %s es %s pero después de esa columana se encontro el valor: %s",
                                                initDateOfFortnight.getMonth(),
                                                initDateOfFortnight.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(),
                                                cellValue
                                        ));
                            }
                            break;
                        }
                    }


                    if (!CellsValidator.isValidTimeRange(cellValue) && !CellsValidator.isValidAbsenceReasons(cellValue)) {
                        errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                                String.format("-> " +cellValue + " <- no, es un valor valido",
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
        return errorsMap;

    }

    private Employee processSchedule( String schedule, LocalDate date,Employee employee) {

        String[] times = schedule.split(" a");
        LocalDateTime startTime = parseTime(times[0].trim(), date);
        LocalDateTime endTime = parseTime(times[1].trim(), date);

        if(endTime.isBefore(startTime))
            endTime = endTime.plusDays(1);

        if (hoursWorkedPerWeek >= MAX_HOURS_BY_WEEK && startTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            for (OvertimeSurcharge overtimeSurcharge : OvertimeSurchargeCalculator.getOvertimeSurchargeList(startTime, endTime)) {
                if (overtimeSurcharge.getQuantityOfHours() != 0) {
                    employee.addNewOverTimeSurcharge(overtimeSurcharge);
                }
            }
        } else {
            for (Surcharge surcharge : SurchargeCalculator.getSurchargeList(startTime, endTime)) {
                if (surcharge.getQuantityOfHours() != 0) {
                    employee.addNewSurcharge(surcharge);
                }
            }

            for (Overtime overtime : OvertimeCalculator.getOvertimeList(startTime, endTime)) {
                if (overtime.getQuantityOfHours() != 0) {
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
