package com.maxiaseo.accounting.domain.api.usecase;

import com.maxiaseo.accounting.domain.api.IPayrollServicesPort;
import com.maxiaseo.accounting.domain.exception.IncorrectFormatExcelValuesException;
import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.model.FileModel;
import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import com.maxiaseo.accounting.domain.spi.IFilePersistencePort;
import com.maxiaseo.accounting.domain.util.file.FileAdministrator;
import com.maxiaseo.accounting.domain.util.file.FileDataProcessor;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PayrollServices implements IPayrollServicesPort {

    private final IExelManagerPort excelManagerAdapter;
    private final FileDataProcessor fileDataProcessor;
    private final IFilePersistencePort filePersistence;

    public PayrollServices(IExelManagerPort excelManagerAdapter, FileDataProcessor fileDataProcessor, IFilePersistencePort filePersistencePort) {
        this.excelManagerAdapter = excelManagerAdapter;
        this.fileDataProcessor = fileDataProcessor;
        this.filePersistence = filePersistencePort;
    }

    public List<Employee> handleFileUpload( String tempFileName, Integer year, Integer month, Integer initDay) throws IOException {

        byte[] dataInMemory = FileAdministrator.getDataInMemoryFromTempFileByName(tempFileName);

        List<List<String>> listOfListData = excelManagerAdapter.getDataFromExcelFileInMemory(dataInMemory);

        List<Employee> employees = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay);

        dataInMemory = excelManagerAdapter.updateEmployeeDataInExcel(dataInMemory, employees );

        FileAdministrator.overwriteTempFile(tempFileName, dataInMemory);

        return employees;
    }

    public FileModel saveFile(InputStream fis,Integer year,Integer month,Integer day) throws IOException {

        byte[] dataInMemory = FileAdministrator.saveInMemory(fis);

        List<List<String>> listOfListData = excelManagerAdapter.getDataFromExcelFileInMemory(dataInMemory);

        fileDataProcessor.resetErrorsMap();

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListData);

        if( ! errorsMap.isEmpty()) {
            throw new IncorrectFormatExcelValuesException("", errorsMap );
        }

        File fileSaved = FileAdministrator.saveTemporaryFileFromInMemoryBytes(dataInMemory, "uploaded-");

        FileModel fileModelSaved = new FileModel();
        fileModelSaved.setName(fileSaved.getName());
        fileModelSaved.setUploadTime(LocalDateTime.now());
        fileModelSaved.setFortNightDate(LocalDate.of(year,month,day));

        filePersistence.addFile(fileModelSaved);

        return fileModelSaved;

    }

    public void deleteTemporaryFile(String fileName) {
        filePersistence.deleteFile(fileName);
        FileAdministrator.deleteTempFileByName (fileName);
    }

    @Override
    public File processSiigoFormat(String tempFileName,  Integer year, Integer month, Integer initDay) throws IOException {

        byte[] dataInMemory = FileAdministrator.getDataInMemoryFromTempFileByName(tempFileName);

        List<List<String>> listOfListData = excelManagerAdapter.getDataFromExcelFileInMemory(dataInMemory);

        List<Employee> employees = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay);

        dataInMemory = FileAdministrator.getSiigoFormat();

        dataInMemory = excelManagerAdapter.populateSiigtoFormat(employees, dataInMemory);

        return FileAdministrator.saveTemporaryFileFromInMemoryBytes( dataInMemory, "siigoPopulate-");

    }

    @Override
    public List<FileModel> getFiles() {
        return filePersistence.getFiles();
    }


}
