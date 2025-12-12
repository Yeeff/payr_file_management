package com.horizonx.file_services.adapters.driven.apachepoi.adapter;

import com.horizonx.file_services.adapters.driving.http.dto.EmployeeOvertimeDto;
import com.horizonx.file_services.configuration.Constants;
import com.horizonx.file_services.domain.model.*;
import com.horizonx.file_services.domain.spi.IExelManagerPort;
import com.horizonx.file_services.domain.util.ConstantsDomain;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelManagerAdapter implements IExelManagerPort {

    private final int INDEX_EMPLOYEE_DOCUMENT_ID = 1;
    private final int INDEX_EMPLOYEE_NEW = 2;
    private final int INDEX_EMPLOYEE_NEW_QUANTITY = 4;


    Workbook workbook;
    Sheet sheet = null;

    public byte[] updateEmployeeDataInExcel(byte[] excelData, List<Employee> employees) throws IOException {
        // Load the Excel file into memory
        ByteArrayInputStream inputStream = new ByteArrayInputStream(excelData);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Add headers to the first row (if not present)
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            headerRow = sheet.createRow(0);
        }

        int headerColIndex = Constants.INDEX_TO_START_TO_WRITE_DATA;
        for (String header : Constants.HEADERS_TO_NAME_OVERTIME_SURCHARGES) {
            Cell headerCell = headerRow.createCell(headerColIndex++, CellType.STRING);
            headerCell.setCellValue(header);
        }

        // Map Employee names to objects for quick lookup
        Map<String, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getName, employee -> employee));

        // Iterate through rows in the sheet
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            Cell nameCell = row.getCell(ConstantsDomain.EMPLOYEE_NAME_INDEX);
            if (nameCell == null || nameCell.getCellType() != CellType.STRING) continue;

            String employeeName = nameCell.getStringCellValue().trim();
            Employee employee = employeeMap.get(employeeName);

            if (employee != null) {
                // Extract totals using reflection
                int colIndex = Constants.INDEX_TO_START_TO_WRITE_DATA;

                for (String methodName : Constants.ORDERED_METHODS_NAMES_TO_RETRIEVE_OVERTIME_SURCHARGES) {
                    try {
                        Method method = Employee.class.getMethod(methodName);
                        Object result = method.invoke(employee);

                        if (result instanceof Double ) {
                            Cell cell = row.createCell(colIndex++, CellType.NUMERIC);
                            cell.setCellValue((Double) result);
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace(); // Handle exceptions appropriately
                    }
                }
            }
        }

        // Write the updated workbook to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    public List<List<String>> getDataFromExcelFileInMemory(byte[] inMemoryFile) throws IOException {
        try (InputStream workbookStream = new ByteArrayInputStream(inMemoryFile)) {
            workbook = WorkbookFactory.create(workbookStream);
        }

        sheet = workbook.getSheetAt(0);
        List<List<String>> result = new ArrayList<>();

        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            boolean isRowEmpty = true;

            for (Cell cell : row) {
                String cellValue = getCellValue(cell).trim();
                rowData.add(cellValue);

                if (!cellValue.isEmpty()) {
                    isRowEmpty = false;
                }
            }

            // Add the row if it's not empty
            if (!isRowEmpty) {
                // Remove trailing empty cells
                int lastNonEmptyIndex = -1;
                for (int i = rowData.size() - 1; i >= 0; i--) {
                    if (!rowData.get(i).isEmpty()) {
                        lastNonEmptyIndex = i;
                        break;
                    }
                }
                result.add(rowData.subList(0, lastNonEmptyIndex + 1));
            }
        }

        return result;
    }

    public byte[] populateSiigtoFormat(List<Employee> employees,  byte[] siigoFormat) throws IOException {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(siigoFormat);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);


        int rowIndex = 5;

        // Iterate through employees and generate rows
        for (Employee employee : employees) {
            // Add surcharge records
            for (Surcharge surcharge : employee.getSurcharges()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(INDEX_EMPLOYEE_DOCUMENT_ID).setCellValue(employee.getId());
                row.createCell(INDEX_EMPLOYEE_NEW).setCellValue(getSurchargeDescription(surcharge));
                row.createCell(INDEX_EMPLOYEE_NEW_QUANTITY).setCellValue(surcharge.getQuantityOfMinutes() );

            }

            // Add overtime records
            for (Overtime overtime : employee.getOvertimes()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(INDEX_EMPLOYEE_DOCUMENT_ID   ).setCellValue(employee.getId());
                row.createCell(INDEX_EMPLOYEE_NEW).setCellValue(getOvertimeDescription(overtime));
                row.createCell(INDEX_EMPLOYEE_NEW_QUANTITY).setCellValue(overtime.getQuantityOfMinutes() );
            }

            // Add overtime surcharge records
            for (OvertimeSurcharge overtimeSurcharge : employee.getOvertimeSurcharges()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(INDEX_EMPLOYEE_DOCUMENT_ID).setCellValue(employee.getId());
                row.createCell(INDEX_EMPLOYEE_NEW).setCellValue(getOvertimeSurchargeDescription(overtimeSurcharge));
                row.createCell(INDEX_EMPLOYEE_NEW_QUANTITY).setCellValue(overtimeSurcharge.getQuantityOfMinutes() );
            }

            // Add absenteeism reason records
            for (AbsenteeismReason absenteeismReason : employee.getAbsenteeismReasons()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(INDEX_EMPLOYEE_DOCUMENT_ID).setCellValue(employee.getId());
                row.createCell(INDEX_EMPLOYEE_NEW).setCellValue(getAbsenteeismReasonDescription(absenteeismReason));
                row.createCell(INDEX_EMPLOYEE_NEW_QUANTITY).setCellValue(absenteeismReason.getQuantityOfHours() );
            }
        }

        // Adjust column width
        sheet.autoSizeColumn(INDEX_EMPLOYEE_DOCUMENT_ID);
        sheet.autoSizeColumn(INDEX_EMPLOYEE_NEW);

        // Write workbook to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    // Helper methods to generate descriptions
    private String getSurchargeDescription(Surcharge surcharge) {
        switch (surcharge.getSurchargeTypeEnum()) {
            case NIGHT:
                return ConstantsDomain.SURCHARGE_NIGHT_SIIGO_NEW;
            case HOLIDAY:
                return ConstantsDomain.SURCHARGE_HOLIDAY_SIIGO_NEW;
            case NIGHT_HOLIDAY:
                return ConstantsDomain.SURCHARGE_NIGHT_HOLIDAY_SIIGO_NEW;
            default:
                return "Surcharge - Ingreso";
        }
    }

    private String getOvertimeDescription(Overtime overtime) {
        switch (overtime.getOvertimeTypeEnum()) {
            case DAY:
                return ConstantsDomain.OVERTIME_DAY_SIIGO_NEW ;
            case NIGHT:
                return ConstantsDomain.OVERTIME_NIGHT_SIIGO_NEW;
            case HOLIDAY:
                return ConstantsDomain.OVERTIME_HOLIDAY_SIIGO_NEW;
            case NIGHT_HOLIDAY:
                return ConstantsDomain.OVERTIME_NIGHT_HOLIDAY_SIIGO_NEW;
            default:
                return "Overtime - Ingreso";
        }
    }

    private String getOvertimeSurchargeDescription(OvertimeSurcharge overtimeSurcharge) {
        switch (overtimeSurcharge.getOvertimeSurchargeTypeEnum()) {
            case NIGHT_HOLIDAY:
                return ConstantsDomain.SURCHARGE_OVERTIME_NIGHT_HOLIDAY_SIIGO_NEW;
            case HOLIDAY:
                return ConstantsDomain.SURCHARGE_OVERTIME_HOLIDAY_SIIGO_NEW;
            default:
                return "Overtime Surcharge - Ingreso";
        }
    }

    private String getAbsenteeismReasonDescription(AbsenteeismReason absenteeismReason) {
        switch (absenteeismReason.getAbsenceReasonsEnum()) {
            case INC_ARL:
                return ConstantsDomain.ABSENTEEISM_INCAPACITY_ARL;
            case INC:
                return ConstantsDomain.ABSENTEEISM_INCAPACITY_WITH_SUPPORT;
            case INC_SIN_SOPR:
                return ConstantsDomain.ABSENTEEISM_INCAPACITY_WITHOUT_SUPPORT;
            case PNR:
                return ConstantsDomain.ABSENTEEISM_UNPAID_LEAVE;
            case LR:
                return ConstantsDomain.ABSENTEEISM_PAID_LEAVE;
            case AUS:
                return ConstantsDomain.ABSENTEEISM;
            case EPS:
                return ConstantsDomain.ABSENTEEISM_EPS_COLLABORATOR;
            case RET:
                return ConstantsDomain.ABSENTEEISM_QUIT;
            case DESC:
                return ConstantsDomain.ABSENTEEISM_DAY_OFF;
            default:
                return "Novedad - Ingreso";
        }
    }

    private String getCellValue(Cell cell) {
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
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public byte[] createEmployeeOvertimeExcel(List<EmployeeOvertimeDto> employees) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employee Overtime Report");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID", "Name", "Total Surcharge Hours Night", "Total Surcharge Hours Holiday",
                "Total Surcharge Hours Night Holiday", "Total Overtime Surcharge Hours Night Holiday",
                "Total Overtime Surcharge Hours Holiday", "Total Overtime Hours Day",
                "Total Overtime Hours Night", "Total Overtime Hours Holiday", "Total Overtime Hours Night Holiday"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Create data rows
        int rowNum = 1;
        for (EmployeeOvertimeDto employee : employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getId());
            row.createCell(1).setCellValue(employee.getName());
            row.createCell(2).setCellValue(employee.getTotalSurchargeHoursNight());
            row.createCell(3).setCellValue(employee.getTotalSurchargeHoursHoliday());
            row.createCell(4).setCellValue(employee.getTotalSurchargeHoursNightHoliday());
            row.createCell(5).setCellValue(employee.getTotalOvertimeSurchargeHoursNightHoliday());
            row.createCell(6).setCellValue(employee.getTotalOvertimeSurchargeHoursHoliday());
            row.createCell(7).setCellValue(employee.getTotalOvertimeHoursDay());
            row.createCell(8).setCellValue(employee.getTotalOvertimeHoursNight());
            row.createCell(9).setCellValue(employee.getTotalOvertimeHoursHoliday());
            row.createCell(10).setCellValue(employee.getTotalOvertimeHoursNightHoliday());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}