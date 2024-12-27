package com.maxiaseo.accounting.domain.util.file;

import com.maxiaseo.accounting.domain.model.*;
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

    public  List<Employee> extractEmployeeData(List<List<String>> listOfListData, Integer year, Integer month, Integer initDay){

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

                    if (CellsValidator.isValidTimeRange(cellValue) ){
                        TimeRange currentTimeRange = getInitTimeAndEndTime(cellValue, currentDate);
                        addSurchargeOvertimesToEmployeeBasedOnTimeRange( currentTimeRange.getStartTime(), currentTimeRange.getEndTime());

                        addHoursWorkedBasedOnTimeRange(currentTimeRange);
                    } else if(!cellValue.equals(AbsenceReasonsEnum.AUS.toString())  ) {
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
                                                      List<List<String >> listOfListExcelData
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


                if (!CellsValidator.isValidTimeRange(cellValue) && !CellsValidator.isValidAbsenceReasons(cellValue)) {
                    errorsMap.put(CellsValidator.getExcelCoordinate(i,j),
                            String.format("-> %s <- no, es un valor valido",
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
            if (overtimeSurcharge.getQuantityOfHours() != 0) {
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
                    , "El campo nombre no puede estar vacio.");
        }
    }

    private  void checkForErrorsInEmployeeDocument(List<String> dataRowList, Integer i){
        String employeeDocumentId =  dataRowList.get(EMPLOYEE_DOCUMENT_ID_INDEX) ;
        if(!CellsValidator.isANumber(employeeDocumentId)){
            errorsMap.put( CellsValidator.getExcelCoordinate(i, EMPLOYEE_DOCUMENT_ID_INDEX),
                    String.format("El valor '%s' no es válido como numero de identificacion del empleado.",
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
                            String.format("El ultimo dia la quincena debe ser 15 pero después de esa columana se encontro el valor: %s",
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
                        String.format("El ultimo dia de %s es %s pero después de esa columana se encontro el valor: %s",
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
                errorsMap.put(CellsValidator.getExcelRow(i), "La fila " + i + " no contiene la candidad de dias correspondiente de la primera quincena");
        }else if (initDateOfFortnight.getDayOfMonth() == FIRST_DAY_OF_SECOND_FORTNIGHT &&
                currentDate.getDayOfMonth() <= lastDayOfSecondFortnight && currentDate.getDayOfMonth() > FIRST_DAY_OF_SECOND_FORTNIGHT ){
            errorsMap.put(CellsValidator.getExcelRow(i), "La fila " + i + " no contiene la candidad de dias correspondiente a la segunda quincena de la fecha indicada");
        }

    }


    class TimeRange {
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;

        public TimeRange(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }
    }
}
