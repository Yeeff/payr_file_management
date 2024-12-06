package com.maxiaseo.accounting.services;

import com.maxiaseo.accounting.domain.exception.IncorrectFormatExcelValuesException;
import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import com.maxiaseo.accounting.domain.util.FileAdministrator;
import com.maxiaseo.accounting.domain.util.FileDataProcessor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class PayrollServices  {

    private final IExelManagerPort excelManagerAdapter;
    private final FileDataProcessor fileDataProcessor;

    public PayrollServices(IExelManagerPort excelManagerAdapter,  FileDataProcessor fileDataProcessor) {
        this.excelManagerAdapter = excelManagerAdapter;
        this.fileDataProcessor =  fileDataProcessor;
    }

    public List<Employee> handleFileUpload( String tempFileName, Integer year, Integer month, Integer initDay) throws IOException {


        byte[] dataInMemory = FileAdministrator.getDataInMemoryFromTempFileByName(tempFileName);

        List<List<String>> listOfListData = excelManagerAdapter.getDataFromExcelFileInMemory(dataInMemory);

        List<Employee> employees = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay);

        dataInMemory = excelManagerAdapter.updateEmployeeDataInExcel(dataInMemory,employees );

        FileAdministrator.overwriteTempFile(tempFileName, dataInMemory);

        return employees;
    }

    public File saveFile(InputStream fis,Integer year,Integer month,Integer day) throws IOException {

        byte[] dataInMemory = FileAdministrator.saveInMemory(fis);

        List<List<String>> listOfListData = excelManagerAdapter.getDataFromExcelFileInMemory(dataInMemory);

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListData);

        if( ! errorsMap.isEmpty())
            throw new IncorrectFormatExcelValuesException("", errorsMap);

        return FileAdministrator.saveTemporaryFileFromInMemoryBytes(dataInMemory);

    }

    //--
    public File getProcessedFile(File tempFile) {
        return tempFile.exists() ? tempFile : null;
    }
    //--
    public void deleteTemporaryFile(File tempFile) {
        if (tempFile.exists()) {
            tempFile.delete();  // Remove the file from the temporary location
        }
    }




}
