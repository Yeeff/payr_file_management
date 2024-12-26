package com.maxiaseo.accounting.domain.util.file;

import com.maxiaseo.accounting.domain.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class FileDataProcessorTest {

    private final FileDataProcessor fileDataProcessor = new FileDataProcessor();

    @Test
    void shouldReturnErrorsForInvalidDataInFirstForNight() {
        // Arrange
        int year = 2024;
        int month = 12;
        int day = 1;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15","16" ),
                Arrays.asList(
                        "15326844a", "", "DESk","7am a 4pm",
                        "7am a 4pm","7am a 4pm","7am a 4p","7am a 4pm","7am a 4pm","DESC",
                        "7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm", "7:30 a 4","extra data"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData);

        assertEquals(6, errorsMap.size()); // Ensure 4 errors are found

        assertTrue(errorsMap.containsKey("B2"));
        assertEquals("El campo nombre no puede estar vacio.", errorsMap.get("B2"));

        assertTrue(errorsMap.containsKey("A2"));
        assertEquals("El valor '15326844a' no es válido como numero de identificacion del empleado.", errorsMap.get("A2"));

        assertTrue(errorsMap.containsKey("C2"));
        assertEquals("-> DESk <- no, es un valor valido", errorsMap.get("C2"));

        assertTrue(errorsMap.containsKey("G2"));
        assertEquals("-> 7am a 4p <- no, es un valor valido", errorsMap.get("G2"));

        assertTrue(errorsMap.containsKey("Q2"));
        assertEquals("-> 7:30 a 4 <- no, es un valor valido", errorsMap.get("Q2"));

        assertTrue(errorsMap.containsKey("R2"));
        assertEquals("El ultimo dia la quincena debe ser 15 pero después de esa columana se encontro el valor: extra data", errorsMap.get("R2"));
    }

    @Test
    void shouldReturnErrorsForInvalidDataInSecondForNight() {
        // Arrange
        int year = 2024;
        int month = 9;
        int day = 16;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE",  "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28","29","30", "31" ),
                Arrays.asList(
                        "1532x6844", "", "7am a 4pm","7am a 4pm",
                        "7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","DESC",
                        "7am a 4pm","7am a 4pm","am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm", "7:30 a 4","extra data"
                )
        );


        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData);

        assertEquals(5, errorsMap.size()); // Ensure 4 errors are found

        assertTrue(errorsMap.containsKey("B2"));
        assertEquals("El campo nombre no puede estar vacio.", errorsMap.get("B2"));

        assertTrue(errorsMap.containsKey("A2"));
        assertEquals("El valor '1532x6844' no es válido como numero de identificacion del empleado.", errorsMap.get("A2"));

        assertTrue(errorsMap.containsKey("M2"));
        assertEquals("-> am a 4pm <- no, es un valor valido", errorsMap.get("M2"));

        assertTrue(errorsMap.containsKey("Q2"));
        assertEquals("-> 7:30 a 4 <- no, es un valor valido", errorsMap.get("Q2"));

        assertTrue(errorsMap.containsKey("R2"));
        assertEquals("El ultimo dia de SEPTEMBER es 30 pero después de esa columana se encontro el valor: extra data", errorsMap.get("R2"));
    }

    @Test
    void shouldReturnNoErrorsForValidData() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 1;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE",  "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28","29","30"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7am a 4pm","7am a 4pm",
                        "7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","DESC",
                        "7am a 4pm","7am a 4pm","5am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm", "7am a 4pm"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData);
        assertTrue(errorsMap.isEmpty());
    }

    @Test
    void  shouldReturnErrorsForIncompleteDataInFirstFortnight() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 1;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE",  "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13","14"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7am a 4pm","7am a 4pm",
                        "7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","DESC",
                        "7am a 4pm","7am a 4pm","5am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData);

        assertEquals(1, errorsMap.size()); // Ensure 4 errors are found

        assertTrue(errorsMap.containsKey("2"));
        assertEquals("La fila 1 no contiene la candidad de dias correspondiente de la primera quincena", errorsMap.get("2"));
    }

    @Test
    void  shouldReturnErrorsForIncompleteDataInSecondFortnight() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 16;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7am a 4pm","7am a 4pm",
                        "7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","DESC",
                        "7am a 4pm","7am a 4pm","5am a 4pm","7am a 4pm","7am a 4pm"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData);

        assertEquals(1, errorsMap.size()); // Ensure 4 errors are found

        assertTrue(errorsMap.containsKey("2"));
        assertEquals("La fila 1 no contiene la candidad de dias correspondiente a la segunda quincena de la fecha indicada", errorsMap.get("2"));
    }

    @Test
    void shouldHandleBlankLine() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 1;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE",  "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28","29","30"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7am a 4pm","7am a 4pm",
                        "7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm","DESC",
                        "7am a 4pm","7am a 4pm","5am a 4pm","7am a 4pm","7am a 4pm","7am a 4pm", "7am a 4pm"
                ),

                Arrays.asList("","","","","","","","","","","","","","",""),

                Arrays.asList(
                        "153xx26844a", "", "DESk","7am a 4pm",
                        "4pm","7am a m","7am ap","7am a m","x","DESC",
                        "m","7am a 4pm","74pm","7am a 4","7m a 4pm","am a 4pm", "7:30 a 4"
                )
        );

        // Act
        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData);

        // Assert
        assertTrue(errorsMap.isEmpty()); // Ensure no errors are found since the blank line stops processing
    }

    @Test void testExtractEmployeeDataWithAbsentCombination1() {
        // Arrange
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"),
                Arrays.asList(
                        "15326844",
                        "NORALDO ISIDRO CARDENAS CARDENAS",
                        "DESC",//Sunday
                        "8pm a 6am",
                        "7am a 4pm",
                        "6pm a 6am",
                        "7am a 4pm",
                        "7am a 4pm",
                        "6pm a 5am",
                        "DESC",//Sunday
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "AUS",
                        "7am a 6pm",
                        "8pm a 4am",
                        "7am a 7pm"//Sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);
        assertEquals("15326844", employee.getId().toString());
        assertEquals("NORALDO ISIDRO CARDENAS CARDENAS", employee.getName());

        assertEquals(18, employee.getTotalSurchargeHoursNight());
        assertEquals(6, employee.getTotalSurchargeHoursNightHoliday() );
        assertEquals(8, employee.getTotalSurchargeHoursHoliday());

        assertEquals(9, employee.getTotalOvertimeHoursDay());
        assertEquals(6, employee.getTotalOvertimeHoursNight());
        assertEquals(4, employee.getTotalOvertimeHoursHoliday());
        assertEquals(3, employee.getTotalOvertimeHoursNightHoliday());
    }

    @Test
    void testExtractEmployeeDataWithAbsentCombination2() {

        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"),
                Arrays.asList(
                        "15326844",
                        "ISIDRO CARDENAS",
                        "6am a 6pm",//sunday
                        "6am a 6pm"	,
                        "6am a 6pm",
                        "6pm a 6am"	,
                        "AUS",
                        "6pm a 6am"	,
                        "6pm a 6am",
                        "6pm a 6am",//sunday
                        "6pm a 6am"	,
                        "AUS",
                        "AUS",
                        "AUS",
                        "6am a 6pm"	,
                        "6am a 6pm"	,
                        "6am a 6pm" //sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);

        assertEquals("15326844", employee.getId().toString());
        assertEquals("ISIDRO CARDENAS", employee.getName());

        assertEquals(20, employee.getTotalSurchargeHoursNight());
        assertEquals(5, employee.getTotalSurchargeHoursNightHoliday() );
        assertEquals(19, employee.getTotalSurchargeHoursHoliday());

        assertEquals(16, employee.getTotalOvertimeHoursDay());
        assertEquals(16, employee.getTotalOvertimeHoursNight());
        assertEquals(8, employee.getTotalOvertimeHoursHoliday());
        assertEquals(4, employee.getTotalOvertimeHoursNightHoliday());
    }

    @Test
    void testExtractEmployeeDataWithAbsentCombination3() {
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
                Arrays.asList(
                        "12345678",
                        "JUAN PEREZ",
                        "8am a 8pm", // Sunday
                        "8am a 5pm",
                        "AUS",
                        "9pm a 5am",//
                        "AUS",
                        "8am a 5pm",
                        "8pm a 4am",//
                        "8am a 8pm", // Sunday
                        "9pm a 5am",//
                        "AUS",
                        "6am a 10am",
                        "9pm a 1am",//
                        "9am a 9pm",
                        "10pm a 6am",//
                        "8am a 5pm" // Sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);

        assertEquals("12345678", employee.getId().toString());
        assertEquals("JUAN PEREZ", employee.getName());

        assertEquals(25, employee.getTotalSurchargeHoursNight());
        assertEquals(10, employee.getTotalSurchargeHoursNightHoliday());
        assertEquals(24, employee.getTotalSurchargeHoursHoliday());

        assertEquals(6, employee.getTotalOvertimeHoursDay());
        assertEquals(0, employee.getTotalOvertimeHoursNight());
        assertEquals(9, employee.getTotalOvertimeHoursHoliday());
        assertEquals(0, employee.getTotalOvertimeHoursNightHoliday());
    }


    @Test
    void testExtractEmployeeDataWithNoSurchargesAndNoOvertimes() {
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
                Arrays.asList(
                        "12345678",
                        "JUAN PEREZ",
                        "DESC", // Sunday
                        "86am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "DESC", // Sunday
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",//
                        "DESC" // Sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay);

        assertNotNull(result);
        assertEquals(0, result.size());

        assertEquals(true, result.isEmpty() );
    }

    @Test
    void testExtractEmployeeDataWithCompensatoryDays() {
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
                Arrays.asList(
                        "12345678",
                        "JUAN PEREZ",
                        "DESC", // Sunday
                        "8am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "DESC", // Sunday
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",
                        "DESC",//
                        "6am a 2pm",
                        "6am a 2pm",//
                        "2am a 2pm" // Sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);

        assertEquals("12345678", employee.getId().toString());
        assertEquals("JUAN PEREZ", employee.getName());

        assertEquals(0, employee.getTotalSurchargeHoursNight());
        assertEquals(0, employee.getTotalSurchargeHoursNightHoliday());
        assertEquals(0, employee.getTotalSurchargeHoursHoliday());

        assertEquals(0, employee.getTotalOvertimeHoursDay());
        assertEquals(0, employee.getTotalOvertimeHoursNight());
        assertEquals(0, employee.getTotalOvertimeHoursHoliday());
        assertEquals(0, employee.getTotalOvertimeHoursNightHoliday());

        assertEquals(8, employee.getTotalOvertimeSurchargeHours_Holiday());
        assertEquals(4, employee.getTotalOvertimeSurchargeHours_NightHoliday());

    }


}