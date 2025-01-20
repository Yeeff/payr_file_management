package com.maxiaseo.accounting.adapters.driven.apachepoi.adapter;

import com.maxiaseo.accounting.domain.model.*;
import com.maxiaseo.accounting.domain.util.ConstantsDomain;
import com.maxiaseo.accounting.domain.util.ConstantsDomain.SurchargeTypeEnum;
import com.maxiaseo.accounting.domain.util.file.FileAdministrator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ExcelManagerAdapterTest {

    /*@Test
    void testPopulateSiigtoFormat() throws Exception {
        int documentColumnIndex = 1;
        int newColumnIndex = 2;

        // Step 1: Prepare mock employees
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee();
        employee1.setId(98714087L);

        employee1.addNewSurcharge(new Surcharge(SurchargeTypeEnum.NIGHT, 2L));
        employee1.addNewSurcharge(new Surcharge(SurchargeTypeEnum.HOLIDAY, 2L));
        employee1.addNewSurcharge(new Surcharge(SurchargeTypeEnum.NIGHT_HOLIDAY, 2L));

        employee1.addNewOverTime(new Overtime(ConstantsDomain.OvertimeTypeEnum.DAY, 3L));
        employee1.addNewOverTime(new Overtime(ConstantsDomain.OvertimeTypeEnum.NIGHT, 3L));
        employee1.addNewOverTime(new Overtime(ConstantsDomain.OvertimeTypeEnum.HOLIDAY, 3L));
        employee1.addNewOverTime(new Overtime(ConstantsDomain.OvertimeTypeEnum.NIGHT_HOLIDAY, 3L));

        employee1.addNewOverTimeSurcharge(new OvertimeSurcharge(ConstantsDomain.OvertimeSurchargeTypeEnum.HOLIDAY, 1L));
        employee1.addNewOverTimeSurcharge(new OvertimeSurcharge(ConstantsDomain.OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY, 1L));

        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.INC_ARL, 2L));
        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.INC, 2L));
        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.INC_SIN_SOPR, 2L));
        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.PNR, 2L));
        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.LR, 2L));
        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.AUS, 2L));
        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.EPS, 2L));
        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.RET, 2L));
        employee1.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.DESC, 2L));

        employees.add(employee1);

        // Step 3: Call the method
        ExcelManagerAdapter generator = new ExcelManagerAdapter();
        byte[] result = generator.populateSiigtoFormat(employees, FileAdministrator.getSiigoFormat());

        // Step 4: Validate the output
        Workbook resultWorkbook = new XSSFWorkbook(new ByteArrayInputStream(result));
        Sheet resultSheet = resultWorkbook.getSheetAt(0);

        // Check the header row remains intact
        assertEquals("Identificación del empleado", resultSheet.getRow(4).getCell(documentColumnIndex).getStringCellValue());
        assertEquals("¿Qué novedad le vas a cargar?", resultSheet.getRow(4).getCell(newColumnIndex).getStringCellValue());

        // Step 5: Prepare expected values
        List<String> expectedValues = Arrays.asList(
                ConstantsDomain.SURCHARGE_NIGHT_SIIGO_NEW,
                ConstantsDomain.SURCHARGE_HOLIDAY_SIIGO_NEW,
                ConstantsDomain.SURCHARGE_NIGHT_HOLIDAY_SIIGO_NEW,
                ConstantsDomain.OVERTIME_DAY_SIIGO_NEW,
                ConstantsDomain.OVERTIME_NIGHT_SIIGO_NEW,
                ConstantsDomain.OVERTIME_HOLIDAY_SIIGO_NEW,
                ConstantsDomain.OVERTIME_NIGHT_HOLIDAY_SIIGO_NEW,
                ConstantsDomain.SURCHARGE_OVERTIME_HOLIDAY_SIIGO_NEW,
                ConstantsDomain.SURCHARGE_OVERTIME_NIGHT_HOLIDAY_SIIGO_NEW,
                ConstantsDomain.ABSENTEEISM_INCAPACITY_ARL,
                ConstantsDomain.ABSENTEEISM_INCAPACITY_WITH_SUPPORT,
                ConstantsDomain.ABSENTEEISM_INCAPACITY_WITHOUT_SUPPORT,
                ConstantsDomain.ABSENTEEISM_UNPAID_LEAVE,
                ConstantsDomain.ABSENTEEISM_PAID_LEAVE,
                ConstantsDomain.ABSENTEEISM,
                ConstantsDomain.ABSENTEEISM_EPS_COLLABORATOR,
                ConstantsDomain.ABSENTEEISM_QUIT,
                ConstantsDomain.ABSENTEEISM_DAY_OFF
        );

        // Step 6: Validate rows populated for employee1
        for (int i = 0; i < expectedValues.size(); i++) {
            Row dataRow = resultSheet.getRow(5 + i); // Rows start at index 5
            assertNotNull(dataRow);
            assertEquals(98714087L, (long) dataRow.getCell(documentColumnIndex).getNumericCellValue());
            assertEquals(expectedValues.get(i), dataRow.getCell(newColumnIndex).getStringCellValue());
        }

        // Close result workbook
        resultWorkbook.close();
    }

    @Test
    public void testPopulateSiigtoFormatWithThreeEmployees() throws IOException {
        int documentColumnIndex = 1;
        int newColumnIndex = 2;

        // Step 1: Prepare mock employees
        List<Employee> employees = new ArrayList<>();

        // Employee 1
        Employee employee1 = new Employee();
        employee1.setId(12345678L);
        employee1.addNewSurcharge(new Surcharge(SurchargeTypeEnum.NIGHT, 2L));
        employee1.addNewOverTime(new Overtime(ConstantsDomain.OvertimeTypeEnum.DAY, 3L));
        employees.add(employee1);

        // Employee 2
        Employee employee2 = new Employee();
        employee2.setId(23456789L);
        employee2.addNewSurcharge(new Surcharge(SurchargeTypeEnum.HOLIDAY, 1L));
        employee2.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.INC_ARL, 2L));
        employees.add(employee2);

        // Employee 3
        Employee employee3 = new Employee();
        employee3.setId(34567890L);
        employee3.addNewOverTimeSurcharge(new OvertimeSurcharge(ConstantsDomain.OvertimeSurchargeTypeEnum.NIGHT_HOLIDAY, 1L));
        employee3.addNewAbsenteeismReason(new AbsenteeismReason(ConstantsDomain.AbsenceReasonsEnum.LR, 1L));
        employees.add(employee3);

        // Step 2: Call the method
        ExcelManagerAdapter generator = new ExcelManagerAdapter();
        byte[] result = generator.populateSiigtoFormat(employees, FileAdministrator.getSiigoFormat());

        // Step 3: Validate the output
        Workbook resultWorkbook = new XSSFWorkbook(new ByteArrayInputStream(result));
        Sheet resultSheet = resultWorkbook.getSheetAt(0);

        // Validate the header row
        assertEquals("Identificación del empleado", resultSheet.getRow(4).getCell(documentColumnIndex).getStringCellValue());
        assertEquals("¿Qué novedad le vas a cargar?", resultSheet.getRow(4).getCell(newColumnIndex).getStringCellValue());

        // Step 4: Prepare expected data
        Map<Long, List<String>> expectedData = new LinkedHashMap<>();
        expectedData.put(12345678L, Arrays.asList(
                ConstantsDomain.SURCHARGE_NIGHT_SIIGO_NEW,
                ConstantsDomain.OVERTIME_DAY_SIIGO_NEW
        ));
        expectedData.put(23456789L, Arrays.asList(
                ConstantsDomain.SURCHARGE_HOLIDAY_SIIGO_NEW,
                ConstantsDomain.ABSENTEEISM_INCAPACITY_ARL
        ));
        expectedData.put(34567890L, Arrays.asList(
                ConstantsDomain.SURCHARGE_OVERTIME_NIGHT_HOLIDAY_SIIGO_NEW,
                ConstantsDomain.ABSENTEEISM_PAID_LEAVE
        ));

        // Step 5: Validate data rows
        int rowIndex = 5; // Start at the first data row
        for (Map.Entry<Long, List<String>> entry : expectedData.entrySet()) {
            Long employeeId = entry.getKey();
            List<String> novelties = entry.getValue();

            for (String novelty : novelties) {
                Row dataRow = resultSheet.getRow(rowIndex++);
                assertNotNull(dataRow);
                assertEquals((long) employeeId, (long) dataRow.getCell(documentColumnIndex).getNumericCellValue());
                assertEquals(novelty, dataRow.getCell(newColumnIndex).getStringCellValue());
            }
        }

        // Close the workbook
        resultWorkbook.close();
    }*/


}