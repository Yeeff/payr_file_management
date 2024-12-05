package com.maxiaseo.accounting.domain.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellsValidator {

    public enum AbsenceReasonsEnum {
        INCAPACIDAD_CON_SOPORTE,
        INCAPACIDAD_SIN_SOPORTE,
        PERMISO_NO_REMUNERADO,
        LICENCIA_REMUNERADA,
        AUSENTISMO,
        COLABORADOR_EN_EPS,
        RETIRO
    }

    public static final Integer NUM_OF_DAYS_IN_A_FORTNIGHT = 15;

    private static final Set<String> VALID_CODES = Set.of(
            "INC ARL", "INC", "INC SIN SOPR", "PNR", "LR", "AUS", "EPS", "RET" , "DESC"
    );

    public static boolean isValidTimeRange(String timeRange) {


        // Regular expression to match valid time range in 12-hour format, with "am" or "pm"
        String timePattern = "^([1-9]|1[0-2])([ap]m) a ([1-9]|1[0-2])([ap]m)$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher = pattern.matcher(timeRange);

        // Return true if it matches, otherwise false
        return matcher.matches();
    }

    public static boolean isValidAbsenceReasons(String value) {
        return VALID_CODES.contains(value.trim());
    }

    public static boolean isAEmptyLinex(Row row){

        Integer blankCellsCounter = 0;
        Boolean isBlankLine = false;

        for (int i = 0; i < row.getLastCellNum(); i++) {
            if( getCellValueAsString(row.getCell(i))  == "" ){
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
    public static boolean isAEmptyLine(List rowDataList){

        Integer blankCellsCounter = 0;
        Boolean isBlankLine = false;

        for (int i = 0; i < rowDataList.size(); i++) {
            if( rowDataList.get(i)  == "" ){
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

    public static String getCellValueAsString(Cell cell) {
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
                    return bd.setScale(0, RoundingMode.HALF_UP).toPlainString();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

}


