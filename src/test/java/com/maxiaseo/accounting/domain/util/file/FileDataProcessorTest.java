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

        System.out.println(errorsMap);

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

    @Test
    void testExtractEmployeeData_validInput() {
        // Arrange
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"),
                Arrays.asList(
                        "15326844",
                        "NORALDO ISIDRO CARDENAS CARDENAS",
                        "DESC",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "DESC",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm"
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
        //assertEquals(5, employee.getTotal());
    }
/*
    @Test
    void testExtractEmployeeData_missingEmployeeData() {
        // Arrange
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3"),
                Arrays.asList("", "", "DESC", "7am a 4pm", "7am a 4pm") // Invalid row
        );
        int year = 2023;
        int month = 12;
        int initDay = 1;

        // Act
        List<Employee> result = yourClass.extractEmployeeData(listOfListData, year, month, initDay);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Invalid row should not create an Employee.
    }

    @Test
    void testExtractEmployeeData_invalidTimeRange() {
        // Arrange
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3"),
                Arrays.asList("15326844", "NORALDO ISIDRO CARDENAS CARDENAS", "DESC", "InvalidTimeRange", "7am a 4pm")
        );
        int year = 2023;
        int month = 12;
        int initDay = 1;

        // Act
        List<Employee> result = yourClass.extractEmployeeData(listOfListData, year, month, initDay);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);
        assertTrue(employee.getSurcharges().isEmpty()); // Invalid time range should not process as a surcharge.
    }
*/

}