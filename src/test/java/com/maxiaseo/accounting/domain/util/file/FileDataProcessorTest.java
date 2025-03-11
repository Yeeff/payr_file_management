package com.maxiaseo.accounting.domain.util.file;

import com.maxiaseo.accounting.domain.service.file.FileDataProcessor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.maxiaseo.accounting.domain.util.ConstantsDomain.*;
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
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"),
                Arrays.asList(
                        "15326844a", "", "DESk", "7am a 4pm",
                        "7am a 4pm", "7am a 4pm", "7am a 4p", "7am a 4pm", "7am a 4pm", "DESC",
                        "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7:30 a 4", "extra data"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData, TimeFormat.REGULAR);

        assertEquals(6, errorsMap.size()); // Ensure 4 errors are found

        assertTrue(errorsMap.containsKey("B2"));
        assertEquals(EMPTY_NAME_VALUE_MESSAGE_ERROR, errorsMap.get("B2"));

        assertTrue(errorsMap.containsKey("A2"));
        assertEquals(String.format(DOCUMENT_ID_VALUE_MESSAGE_ERROR, "15326844a"), errorsMap.get("A2"));

        assertTrue(errorsMap.containsKey("C2"));
        assertEquals(String.format(INVALID_VALUE_MESSAGE_ERROR, "DESk"), errorsMap.get("C2"));

        assertTrue(errorsMap.containsKey("G2"));
        assertEquals(String.format(INVALID_VALUE_MESSAGE_ERROR, "7am a 4p"), errorsMap.get("G2"));

        assertTrue(errorsMap.containsKey("Q2"));
        assertEquals(String.format(INVALID_VALUE_MESSAGE_ERROR, "7:30 a 4"), errorsMap.get("Q2"));

        assertTrue(errorsMap.containsKey("R2"));
        assertEquals(String.format(LAST_VALUE_FIRST_FORTNIGHT_MESSAGE_ERROR, "extra data"), errorsMap.get("R2"));
    }

    @Test
    void shouldReturnErrorsForInvalidDataInSecondForNight() {
        // Arrange
        int year = 2024;
        int month = 9;
        int day = 16;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"),
                Arrays.asList(
                        "1532x6844", "", "7am a 4pm", "7am a 4pm",
                        "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "DESC",
                        "7am a 4pm", "7am a 4pm", "am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7:30 a 4", "extra data"
                )
        );


        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData, TimeFormat.REGULAR);

        assertEquals(5, errorsMap.size()); // Ensure 4 errors are found

        assertTrue(errorsMap.containsKey("B2"));
        assertEquals(EMPTY_NAME_VALUE_MESSAGE_ERROR, errorsMap.get("B2"));

        assertTrue(errorsMap.containsKey("A2"));
        assertEquals(String.format(DOCUMENT_ID_VALUE_MESSAGE_ERROR, "1532x6844"), errorsMap.get("A2"));

        assertTrue(errorsMap.containsKey("M2"));
        assertEquals(String.format(INVALID_VALUE_MESSAGE_ERROR, "am a 4pm"), errorsMap.get("M2"));

        assertTrue(errorsMap.containsKey("Q2"));
        assertEquals(String.format(INVALID_VALUE_MESSAGE_ERROR, "7:30 a 4"), errorsMap.get("Q2"));

        assertTrue(errorsMap.containsKey("R2"));
        assertEquals(String.format(LAST_VALUE_SECOND_FORTNIGHT_MESSAGE_ERROR, "SEPTEMBER", "30", "extra data"), errorsMap.get("R2"));
    }

    @Test
    void shouldReturnNoErrorsForValidData() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 1;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7am a 4pm", "7am a 4pm",
                        "7am a 4:30pm", "7:30am a 4pm", "7am a 4pm", "7am a 4:30pm", "7am a 4pm", "DESC",
                        "7am a 4pm", "7am a 4pm", "5:30am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7:30am a 4pm"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData, TimeFormat.REGULAR);
        assertTrue(errorsMap.isEmpty());
    }

    @Test
    void shouldReturnNoErrorsForValidDataWithMilitaryFormat() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 1;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7:00 a 4:00", "7:00 a 4:00",
                        "7 a 4:30", "7:30 a 4:00", "7:00 a 4:00", "7 a 4:30", "13 a 4:00", "DESC",
                        "7:00 a 4:00", "7:00 a 16", "5:30 a 4", "7:00 a 4:30", "23 a 4:00", "7 a 4:00", "7:30 a 4:00"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData, TimeFormat.MILITARY);
        assertTrue(errorsMap.isEmpty());
    }

    @Test
    void shouldReturnErrorsForIncompleteDataInFirstFortnight() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 1;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7am a 4pm", "7am a 4pm",
                        "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "DESC",
                        "7am a 4pm", "7am a 4pm", "5am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData, TimeFormat.REGULAR);

        assertEquals(1, errorsMap.size()); // Ensure 4 errors are found

        assertTrue(errorsMap.containsKey("2"));
        assertEquals(String.format(NOT_CORRESPONDING_QUANTITY_OF_DAYS_FOR_FORTNIGHT_MESSAGE_ERROR, 1), errorsMap.get("2"));
    }

    @Test
    void shouldReturnErrorsForIncompleteDataInSecondFortnight() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 16;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7am a 4pm", "7am a 4pm",
                        "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "DESC",
                        "7am a 4pm", "7am a 4pm", "5am a 4pm", "7am a 4pm", "7am a 4pm"
                )
        );

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData, TimeFormat.REGULAR);

        assertEquals(1, errorsMap.size()); // Ensure 4 errors are found

        assertTrue(errorsMap.containsKey("2"));
        assertEquals(String.format(NOT_CORRESPONDING_QUANTITY_OF_DAYS_FOR_FORTNIGHT_MESSAGE_ERROR, 1), errorsMap.get("2"));
    }

    @Test
    void shouldHandleBlankLine() {
        // Arrange
        int year = 2023;
        int month = 12;
        int day = 1;

        List<List<String>> listOfListExcelData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"),
                Arrays.asList(
                        "15326844", "ANDRES PARRA", "7am a 4pm", "7am a 4pm",
                        "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "DESC",
                        "7am a 4pm", "7am a 4pm", "5am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm", "7am a 4pm"
                ),

                Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""),

                Arrays.asList(
                        "153xx26844a", "", "DESk", "7am a 4pm",
                        "4pm", "7am a m", "7am ap", "7am a m", "x", "DESC",
                        "m", "7am a 4pm", "74pm", "7am a 4", "7m a 4pm", "am a 4pm", "7:30 a 4"
                )
        );

        // Act
        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListExcelData, TimeFormat.REGULAR);

        // Assert
        assertTrue(errorsMap.isEmpty()); // Ensure no errors are found since the blank line stops processing
    }

}


