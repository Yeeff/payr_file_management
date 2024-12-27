package com.maxiaseo.accounting.adapters.driven.apachepoi.adapter;

import com.maxiaseo.accounting.configuration.Constants;
import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import com.maxiaseo.accounting.domain.util.ConstantsDomain;
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

                        if (result instanceof Long) {
                            Cell cell = row.createCell(colIndex++, CellType.NUMERIC);
                            cell.setCellValue((Long) result);
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
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
