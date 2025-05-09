package com.horizonx.file_services.domain.service.file;

import com.horizonx.file_services.domain.util.ConstantsDomain;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellsValidator {

    private CellsValidator() {}

    public static final Integer NUM_OF_DAYS_IN_A_FORTNIGHT = 15;


    public static boolean isValidTimeRange(String timeRange, ConstantsDomain.TimeFormat formatType) {
        String timePattern;

        timePattern = switch (formatType){
            case REGULAR -> "^([1-9]|1[0-2])(:[0-5][0-9])?([ap]m) a ([1-9]|1[0-2])(:[0-5][0-9])?([ap]m)$";
            case MILITARY -> "^([01]?[0-9]|2[0-3])(:[0-5][0-9])? a ([01]?[0-9]|2[0-3])(:[0-5][0-9])?$";
            case MILITARY_WITHOUT_COLON -> "^(?:[01][0-9]|2[0-3])[0-5][0-9] A (?:[01][0-9]|2[0-3])[0-5][0-9]$";
        };

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher = pattern.matcher(timeRange);

        // Return true if it matches, otherwise false
        return matcher.matches();
    }

    public static boolean isValidAbsenceReasons(String value) {
        return ConstantsDomain.VALID_CODES.contains(value.trim());
    }

    public static boolean isAEmptyLine(List<String> rowDataList){

        Integer blankCellsCounter = 0;
        Boolean isBlankLine = false;

        for (int i = 0; i < rowDataList.size(); i++) {
            if( rowDataList.get(i).equals("") ){
                blankCellsCounter++;
            }else{
                break;
            }
            if ( blankCellsCounter >= NUM_OF_DAYS_IN_A_FORTNIGHT){
                isBlankLine = true;
            }
        }

        return isBlankLine;
    }

    public static boolean isANumber(String str){
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String getExcelColumnName(int columnIndex) {
        StringBuilder columnName = new StringBuilder();
        while (columnIndex >= 0) {
            int remainder = columnIndex % 26;
            columnName.insert(0, (char) (remainder + 'A'));
            columnIndex = (columnIndex / 26) - 1;
        }
        return columnName.toString();
    }

    public static String getExcelCoordinate(int rowIndex, int columnIndex) {
        String columnName = getExcelColumnName(columnIndex);
        int excelRow = rowIndex + 1;
        return columnName + excelRow;
    }
    public static String getExcelRow(int rowIndex) {
        int excelRow = rowIndex + 1;
        return "" + excelRow;
    }

}


