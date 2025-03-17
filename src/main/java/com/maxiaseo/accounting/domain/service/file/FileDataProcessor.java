package com.maxiaseo.accounting.domain.service.file;


import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.maxiaseo.accounting.domain.util.ConstantsDomain.*;
import static com.maxiaseo.accounting.domain.util.ConstantsDomain.FIRST_DAY_OF_SECOND_FORTNIGHT;

public class FileDataProcessor {



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


}
