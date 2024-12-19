package com.maxiaseo.accounting.domain.util.file;

import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.model.Overtime;
import com.maxiaseo.accounting.domain.model.OvertimeSurcharge;
import com.maxiaseo.accounting.domain.model.Surcharge;
import com.maxiaseo.accounting.domain.util.OvertimeCalculator;
import com.maxiaseo.accounting.domain.util.OvertimeSurchargeCalculator;
import com.maxiaseo.accounting.domain.util.SurchargeCalculator;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.maxiaseo.accounting.domain.util.ConstantsDomain.*;
import static com.maxiaseo.accounting.domain.util.ConstantsDomain.FIRST_DAY_OF_SECOND_FORTNIGHT;

public class FileDataProcessor {

    private static final Long MAX_HOURS_BY_DAY = 8L;
    private static final Long MAX_HOURS_BY_WEEK = 48L;

    private static final Integer FIRST_DAY_OF_MONTH = 1;
    private static final Integer LAST_DAY_OF_FIRST_FORTNIGHT = 15;


    Long hoursWorkedPerWeek ;

    public FileDataProcessor() {
        this.hoursWorkedPerWeek = 0L;
    }

    public  List<Employee> extractEmployeeData(List<List<String>> listOfListData, Integer year, Integer month, Integer initDay){

        LocalDate initDateOfFortnight = LocalDate.of(year,month, initDay);
        LocalDate currentDate;

        List<Employee> employees = new ArrayList<>();

        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= listOfListData.size()-1; i++) {

            Employee employee = Employee.builder()
                    .surcharges(new ArrayList<>())
                    .overtimes(new ArrayList<>())
                    .overtimeSurcharges(new ArrayList<>())
                    .build();

            currentDate = initDateOfFortnight;

            List<String> dataRowList = listOfListData.get(i);
            if (!dataRowList.isEmpty()) {

                try{
                    employee.setId( Long.valueOf(dataRowList.get(EMPLOYEE_DOCUMENT_ID_INDEX)));
                    employee.setName(dataRowList.get(EMPLOYEE_NAME_INDEX));
                }catch (Exception e){
                    break;
                }

                for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < dataRowList.size(); j++) {

                    if (dataRowList.get(j) != null) {
                        String cellValue = dataRowList.get(j);

                        if (CellsValidator.isValidTimeRange(cellValue) || !cellValue.equals(AbsenceReasonsEnum.AUS)){
                            TimeRange currentTimeRange = getInitTimeAndEndTime(cellValue, currentDate);
                            employee = processSchedule( currentTimeRange.getStartTime(), currentTimeRange.getEndTime(), employee);

                            addHoursWorked(currentTimeRange);
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
            if(
                    !employee.getSurcharges().isEmpty() ||
                            !employee.getOvertimes().isEmpty() ||
                            !employee.getOvertimeSurcharges().isEmpty()
            ) employees.add(employee);

        }
        return employees;
    }

    public  Map<String, String> getErrorsFormat(Integer year, Integer month, Integer day,
                                                      List<List<String >> listOfListExcelData
    ){

        Map<String, String> errorsMap = new HashMap<>();

        Boolean blankLineFound = Boolean.valueOf(false);

        LocalDate initDateOfFortnight = LocalDate.of(year,month, day);
        LocalDate currentDate;
        Integer lastDayOfSecondFortnight = 30;

        if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT)
            lastDayOfSecondFortnight = initDateOfFortnight.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();


        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= listOfListExcelData.size() - 1; i++) {

            currentDate = initDateOfFortnight;

            List<String> dataRowList = listOfListExcelData.get(i);
            if (!dataRowList.isEmpty()) {

                if(dataRowList.get(0).equals("") &&
                        CellsValidator.isAEmptyLine(dataRowList)){
                    blankLineFound = true;
                    break;
                }

                String employeeName = dataRowList.get(EMPLOYEE_NAME_INDEX);
                if(employeeName.equals("")){
                    errorsMap.put(CellsValidator.getExcelCoordinate(i,EMPLOYEE_NAME_INDEX)
                            , "El campo nombre no puede estar vacio.");
                }

                String employeeDocumentId =  dataRowList.get(EMPLOYEE_DOCUMENT_ID_INDEX) ;
                if(!CellsValidator.isANumber(employeeDocumentId)){
                    errorsMap.put( CellsValidator.getExcelCoordinate(i, EMPLOYEE_DOCUMENT_ID_INDEX),
                            String.format("El valor '%s' no es válido como numero de identificacion del empleado.",
                                    employeeDocumentId
                            ));
                }

                for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < dataRowList.size() ; j++) {

                    String cellValue = dataRowList.get(j);

                    //First fortnight of month
                    if(initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_FIRST_FORTNIGHT){
                        if(currentDate.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT){
                            if(!cellValue.equals("")){
                                errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                                        String.format("El ultimo dia la quincena debe ser 15 pero después de esa columana se encontro el valor: %s",
                                                cellValue
                                        ));
                            }
                            break;
                        }

                        //Second fortnight of month
                    } else if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT
                        && currentDate.getDayOfMonth() == FIRST_DAY_OF_MONTH ){

                            if(!cellValue.equals("")){
                                errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                                        String.format("El ultimo dia de %s es %s pero después de esa columana se encontro el valor: %s",
                                                initDateOfFortnight.getMonth(),
                                                initDateOfFortnight.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(),
                                                cellValue
                                        ));
                            }
                            break;

                    }


                    if (!CellsValidator.isValidTimeRange(cellValue) && !CellsValidator.isValidAbsenceReasons(cellValue)) {
                        errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                                String.format("-> %s <- no, es un valor valido",
                                        cellValue
                                ));
                    }

                    currentDate =currentDate.plusDays(1);
                }
            }

            if(initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_FIRST_FORTNIGHT){
                if(currentDate.getDayOfMonth() <= LAST_DAY_OF_FIRST_FORTNIGHT)
                    errorsMap.put(CellsValidator.getExcelRow(i), "La fila " + i + " no contiene la candidad de dias correspondiente de la primera quincena");
            }else if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT &&
                currentDate.getDayOfMonth() <= lastDayOfSecondFortnight && currentDate.getDayOfMonth() > FIRST_DAY_OF_SECOND_FORTNIGHT ){
                    errorsMap.put(CellsValidator.getExcelRow(i), "La fila " + i + " no contiene la candidad de dias correspondiente a la segunda quincena de la fecha indicada");

            }

            if(Boolean.TRUE.equals(blankLineFound))
                break;

        }
        return errorsMap;

    }

    private Employee processSchedule( LocalDateTime startTime, LocalDateTime endTime,Employee employee) {

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

    private TimeRange getInitTimeAndEndTime( String schedule, LocalDate date){
        String[] times = schedule.split(" a");
        LocalDateTime startTime = parseTime(times[0].trim(), date);
        LocalDateTime endTime = parseTime(times[1].trim(), date);

        if(endTime.isBefore(startTime))
            endTime = endTime.plusDays(1);

        return new TimeRange(startTime, endTime);
    }

    private void addHoursWorked(TimeRange currentTimeRange){
        Long hoursWorkedPerDay = Duration.between(
                currentTimeRange.getStartTime(), currentTimeRange.getEndTime() ).toHours();

        if (hoursWorkedPerDay > MAX_HOURS_BY_DAY)
            hoursWorkedPerWeek += MAX_HOURS_BY_DAY;
        else
            hoursWorkedPerWeek += hoursWorkedPerDay ;
    }

}
