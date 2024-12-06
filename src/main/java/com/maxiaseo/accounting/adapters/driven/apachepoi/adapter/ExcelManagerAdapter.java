package com.maxiaseo.accounting.adapters.driven.apachepoi.adapter;

import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public  byte[] updateEmployeeDataInExcel(byte[] excelData, List<Employee> employees) throws IOException {
        // Load the Excel file into memory
        ByteArrayInputStream inputStream = new ByteArrayInputStream(excelData);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        // Map Employee names to objects for quick lookup
        Map<String, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getName, employee -> employee));

        // Iterate through rows in the sheet
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            Cell nameCell = row.getCell(1); // Assuming "NOMBRE" is in column 2 (index 1)
            if (nameCell == null || nameCell.getCellType() != CellType.STRING) continue;

            String employeeName = nameCell.getStringCellValue().trim();
            Employee employee = employeeMap.get(employeeName);

            if (employee != null) {
                // Extract totals using reflection
                int colIndex = 16; // Start writing totals in column 16 (adjust as needed)
                for (Method method : Employee.class.getMethods()) {
                    if (method.getName().startsWith("getTotal") && method.getParameterCount() == 0) {
                        try {
                            Object result = method.invoke(employee);
                            if (result instanceof Long) {
                                Cell cell = row.createCell(colIndex++, CellType.NUMERIC);
                                cell.setCellValue((Long) result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public List<List<String>> getDataFromExcelFileInMemory(byte[] inMemoryFile)throws IOException {
        try (InputStream workbookStream = new ByteArrayInputStream(inMemoryFile)) {
            workbook = WorkbookFactory.create(workbookStream);
        }

        sheet = workbook.getSheetAt(0);
        List<List<String>> result = new ArrayList<>();


        sheet = workbook.getSheetAt(0);

        // Iterate over each row
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();

            // Iterate over each cell in the row
            for (Cell cell : row) {
                String cellValue = getCellValue(cell);
                rowData.add(cellValue);
            }

            result.add(rowData);
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
