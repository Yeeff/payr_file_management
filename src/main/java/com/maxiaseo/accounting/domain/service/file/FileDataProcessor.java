package com.maxiaseo.accounting.domain.service.file;

import com.maxiaseo.accounting.domain.model.*;
import com.maxiaseo.accounting.domain.service.Maxiaseo;
import com.maxiaseo.accounting.domain.service.processor.SurchargeCalculator;
import com.maxiaseo.accounting.domain.util.TimeRange;
import com.maxiaseo.accounting.domain.service.processor.OvertimeCalculator;
import com.maxiaseo.accounting.domain.service.processor.OvertimeSurchargeCalculator;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.maxiaseo.accounting.domain.util.ConstantsDomain.*;
import static com.maxiaseo.accounting.domain.util.ConstantsDomain.FIRST_DAY_OF_SECOND_FORTNIGHT;

public class FileDataProcessor {


    private Employee employee;

    private Integer lastDayOfSecondFortnight;

    Map<String, String> errorsMap ;



    Long hoursWorkedPerWeek ;

    public FileDataProcessor() {
        this.hoursWorkedPerWeek = 0L;
        this.lastDayOfSecondFortnight = 30;
        errorsMap = new HashMap<>();
    }

    public void resetErrorsMap() {
        errorsMap.clear();
    }

    public  List<Employee> extractEmployeeData(List<List<String>> listOfListData, Integer year, Integer month, Integer initDay, TimeFormat timeFormatType){

        LocalDate initDateOfFortnight = LocalDate.of(year,month, initDay);
        LocalDate currentDate;

        List<Employee> employees = new ArrayList<>();

        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= listOfListData.size()-1; i++) {

            employee = new Employee();

            currentDate = initDateOfFortnight;

            List<String> dataRowList = listOfListData.get(i);

            employee.setId(extractDocumentId(dataRowList));
            employee.setName(extractName(dataRowList));

            for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < dataRowList.size(); j++) {

                if (dataRowList.get(j) != null) {
                    String cellValue = dataRowList.get(j);

                    if (CellsValidator.isValidTimeRange(cellValue, timeFormatType) ){
                        TimeRange currentTimeRange = getInitTimeAndEndTime(cellValue, currentDate, timeFormatType);
                        addSurchargeOvertimesToEmployeeBasedOnTimeRange( currentTimeRange.getStartTime(), currentTimeRange.getEndTime());

                        addHoursWorkedBasedOnTimeRange(currentTimeRange);
                    } else if(isValidAbsenceReason(cellValue) && !cellValue.equals(AbsenceReasonsEnum.AUS.toString())  ) {
                        addHoursWorkedBasedOnAbsentReason(MAX_HOURS_BY_DAY);
                        addAbsenteeismReasonToEmployee(cellValue);
                    }
                }

                if(currentDate.getDayOfWeek() == DayOfWeek.SUNDAY)
                    hoursWorkedPerWeek = 0L;

                currentDate =currentDate.plusDays(1);

            }
            employees = addCurrentEmployeeToList(employees);

        }
        return employees;
    }

    public  Map<String, String> getErrorsFormat(Integer year, Integer month, Integer day,
                                                      List<List<String >> listOfListExcelData, TimeFormat timeFormatType
    ){

        LocalDate initDateOfFortnight = LocalDate.of(year,month, day);
        LocalDate currentDate;

        adjustTheLastDayOfSecondFortnightIfNeeded(initDateOfFortnight);

        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= listOfListExcelData.size() - 1; i++) {

            currentDate = initDateOfFortnight;

            List<String> dataRowList = listOfListExcelData.get(i);


            if(Boolean.TRUE.equals( isAnEmptyLine(dataRowList)) ) break;

            checkForErrorsInEmployeeName(dataRowList,i);

            checkForErrorsInEmployeeDocument(dataRowList, i);

            for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < dataRowList.size() ; j++) {

                String cellValue = dataRowList.get(j);

                if (Boolean.TRUE.equals( isThereErrorsAfterTheLastDayOfFortNight(initDateOfFortnight,currentDate,cellValue, i, j)) )
                    break;


                if (!CellsValidator.isValidTimeRange(cellValue, timeFormatType) && !CellsValidator.isValidAbsenceReasons(cellValue)) {
                    errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                            String.format(INVALID_VALUE_MESSAGE_ERROR,
                                    cellValue
                            ));
                }

                currentDate =currentDate.plusDays(1);
            }

            checkTheNumberOfIterationsBasedOnFortnight(initDateOfFortnight, currentDate, i);


        }
        return errorsMap;

    }

    private void addSurchargeOvertimesToEmployeeBasedOnTimeRange(LocalDateTime startTime, LocalDateTime endTime) {

        if (hoursWorkedPerWeek >= MAX_HOURS_BY_WEEK && startTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            addOvertimeSurchargeToEmployee(startTime, endTime);
        } else {
            for (Surcharge surcharge : SurchargeCalculator.getSurchargeList(startTime, endTime)) {
                if (surcharge.getQuantityOfMinutes() != 0) {
                    employee.addNewSurcharge(surcharge);
                }
            }

            for (Overtime overtime : OvertimeCalculator.getOvertimeList(startTime, endTime)) {
                if (overtime.getQuantityOfMinutes() != 0) {
                    employee.addNewOverTime(overtime);
                }
            }
        }

    }

    public static LocalDateTime parseTime(String timeString, LocalDate date, TimeFormat formatType) {
        DateTimeFormatter formatter;

        formatter = switch (formatType){
            case REGULAR -> new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("[h:mma][ha]")
                    .toFormatter(Locale.ENGLISH);

            case MILITARY ->  new DateTimeFormatterBuilder()
                    .appendPattern("[H:mm][H]") // Handle both "14:00" and "14"
                    .toFormatter();

            case MILITARY_WITHOUT_COLON -> new DateTimeFormatterBuilder()
                    .appendPattern("[HHmm]")
                    .toFormatter();
        };

        try {
            // Parse the time string into LocalTime
            LocalTime time = LocalTime.parse(timeString, formatter);

            // Combine the parsed time with the provided date to create a LocalDateTime
            return LocalDateTime.of(date, time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format: " + timeString);
        }
    }


    private TimeRange getInitTimeAndEndTime( String schedule, LocalDate date, TimeFormat timeFormat){

        Maxiaseo maxi = new Maxiaseo();

        String[] times = timeFormat == TimeFormat.MILITARY_WITHOUT_COLON
                ? schedule.split(" A")
                : schedule.split(" a");

        LocalDateTime startTime = parseTime(times[0].trim(), date, timeFormat);
        LocalDateTime endTime = parseTime(times[1].trim(), date, timeFormat);

        if(endTime.isBefore(startTime))
            endTime = endTime.plusDays(1);

        TimeRange timeRange = new TimeRange(startTime, endTime);

        return maxi.validateLunchHour(timeRange) ;
    }

    private void addHoursWorkedBasedOnTimeRange(TimeRange currentTimeRange){
        Long hoursWorkedPerDay = Duration.between(
                currentTimeRange.getStartTime(), currentTimeRange.getEndTime() ).toHours();

        if (hoursWorkedPerDay > MAX_HOURS_BY_DAY)
            hoursWorkedPerWeek += MAX_HOURS_BY_DAY;
        else
            hoursWorkedPerWeek += hoursWorkedPerDay ;
    }

    private void addHoursWorkedBasedOnAbsentReason( Long hourToSum){

            hoursWorkedPerWeek += hourToSum ;
    }

    private String extractName(List<String> dataRowList){
                 return dataRowList.get(EMPLOYEE_NAME_INDEX);
    }

    private Long extractDocumentId(List<String> dataRowList){
        return Long.valueOf(dataRowList.get(EMPLOYEE_DOCUMENT_ID_INDEX));
    }

    private List<Employee> addCurrentEmployeeToList(List<Employee> employees){
        if(
                !employee.getSurcharges().isEmpty() ||
                        !employee.getOvertimes().isEmpty() ||
                        !employee.getOvertimeSurcharges().isEmpty()
        ) employees.add(employee);
        return employees;
    }

    private void addOvertimeSurchargeToEmployee (LocalDateTime startTime, LocalDateTime endTime){
        for (OvertimeSurcharge overtimeSurcharge : OvertimeSurchargeCalculator.getOvertimeSurchargeList(startTime, endTime)) {
            if (overtimeSurcharge.getQuantityOfMinutes() != 0) {
                employee.addNewOverTimeSurcharge(overtimeSurcharge);
            }
        }
    }

    private void addAbsenteeismReasonToEmployee(String reason){
        AbsenteeismReason absenteeismReason = new AbsenteeismReason();
        absenteeismReason.setAbsenceReasonsEnum(AbsenceReasonsEnum.valueOf(reason));
        absenteeismReason.setQuantityOfHours(MAX_HOURS_BY_DAY);
        employee.addNewAbsenteeismReason(absenteeismReason);
    }

    //MethodsToValidateFormat

    private Boolean isAnEmptyLine(List<String> dataRowList){

        Boolean isEmpty = false;

        if(dataRowList.get(0).equals("") &&
                CellsValidator.isAEmptyLine(dataRowList)){
            isEmpty = true;
        }
        return isEmpty;
    }

    private void adjustTheLastDayOfSecondFortnightIfNeeded ( LocalDate initDateOfFortnight){
        if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT)
            lastDayOfSecondFortnight = initDateOfFortnight.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }

    private void checkForErrorsInEmployeeName(List<String> dataRowList, Integer i){
        String employeeName = dataRowList.get(EMPLOYEE_NAME_INDEX);
        if(employeeName.equals("")){
            errorsMap.put(CellsValidator.getExcelCoordinate(i ,EMPLOYEE_NAME_INDEX)
                    , EMPTY_NAME_VALUE_MESSAGE_ERROR);
        }
    }

    private  void checkForErrorsInEmployeeDocument(List<String> dataRowList, Integer i){
        String employeeDocumentId =  dataRowList.get(EMPLOYEE_DOCUMENT_ID_INDEX) ;
        if(!CellsValidator.isANumber(employeeDocumentId)){
            errorsMap.put( CellsValidator.getExcelCoordinate(i, EMPLOYEE_DOCUMENT_ID_INDEX),
                    String.format(DOCUMENT_ID_VALUE_MESSAGE_ERROR,
                            employeeDocumentId
                    ));
        }
    }

    private Boolean isThereErrorsAfterTheLastDayOfFortNight(LocalDate initDateOfFortnight, LocalDate currentDate, String cellValue,Integer i, Integer j){
        //First fortnight of month
        if(initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_FIRST_FORTNIGHT){
            if(currentDate.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT){
                if(!cellValue.equals("")){
                    errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                            String.format(LAST_VALUE_FIRST_FORTNIGHT_MESSAGE_ERROR,
                                    cellValue
                            ));
                }
                return true;
            }

            //Second fortnight of month
        } else if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT
                && currentDate.getDayOfMonth() == FIRST_DAY_OF_MONTH ){

            if(!cellValue.equals("")){
                errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                        String.format(LAST_VALUE_SECOND_FORTNIGHT_MESSAGE_ERROR,
                                initDateOfFortnight.getMonth(),
                                initDateOfFortnight.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(),
                                cellValue
                        ));
            }
            return true;

        }
        return false;
    }

    private void checkTheNumberOfIterationsBasedOnFortnight(LocalDate initDateOfFortnight, LocalDate currentDate, Integer i){

        if(initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_FIRST_FORTNIGHT){
            if(currentDate.getDayOfMonth() <= LAST_DAY_OF_FIRST_FORTNIGHT)
                errorsMap.put(CellsValidator.getExcelRow(i), String.format(NOT_CORRESPONDING_QUANTITY_OF_DAYS_FOR_FORTNIGHT_MESSAGE_ERROR,i));
        }else if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT &&
                currentDate.getDayOfMonth() <= lastDayOfSecondFortnight && currentDate.getDayOfMonth() > FIRST_DAY_OF_SECOND_FORTNIGHT ){
            errorsMap.put(CellsValidator.getExcelRow(i), String.format(NOT_CORRESPONDING_QUANTITY_OF_DAYS_FOR_FORTNIGHT_MESSAGE_ERROR,i));
        }

    }

    private boolean isValidAbsenceReason(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            AbsenceReasonsEnum.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }



}
