package com.maxiaseo.accounting.adapters.driven.apachepoi.adapter;

import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ExcelManagerAdapter implements IExelManagerPort {

    Workbook workbook;
    Sheet sheet = null;

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

    private static String getCellValue(Cell cell) {
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
