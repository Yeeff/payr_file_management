package com.maxiaseo.accounting.domain.api.usecase;

import com.maxiaseo.accounting.domain.api.IPayrollServicesPort;
import com.maxiaseo.accounting.domain.exception.FileProcessingException;
import com.maxiaseo.accounting.domain.exception.FileRetrievalException;
import com.maxiaseo.accounting.domain.exception.IncorrectFormatExcelValuesException;
import com.maxiaseo.accounting.domain.model.FileModel;
import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import com.maxiaseo.accounting.domain.spi.IFilePersistencePort;
import com.maxiaseo.accounting.domain.util.ConstantsDomain;
import com.maxiaseo.accounting.domain.service.file.FileAdministrator;
import com.maxiaseo.accounting.domain.service.file.FileDataProcessor;

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

    public FileModel saveFile(InputStream fis,Integer year,Integer month,Integer day) throws IOException {

        ConstantsDomain.TimeFormat timeFormat = ConstantsDomain.TimeFormat.MILITARY_WITHOUT_COLON;

        byte[] dataInMemory = FileAdministrator.saveInMemory(fis);

        List<List<String>> listOfListData = excelManagerAdapter.getDataFromExcelFileInMemory(dataInMemory);

        fileDataProcessor.resetErrorsMap();

        Map<String, String> errorsMap = fileDataProcessor.getErrorsFormat(year, month, day, listOfListData, timeFormat);

        if( ! errorsMap.isEmpty()) {
            throw new IncorrectFormatExcelValuesException("", errorsMap );
        }

        File fileSaved = FileAdministrator.saveTemporaryFileFromInMemoryBytes(dataInMemory, "uploaded-");

        FileModel fileModelSaved = new FileModel();
        fileModelSaved.setName(fileSaved.getName());
        fileModelSaved.setUploadTime(LocalDateTime.now());
        fileModelSaved.setFortNightDate(LocalDate.of(year,month,day));
        fileModelSaved.setTimeFormat(timeFormat.name());

        filePersistence.addFile(fileModelSaved);

        return fileModelSaved;

    }

    public FileModel getFileContent(String tempFileName) {
        try {
            byte[] dataInMemory = FileAdministrator.getDataInMemoryFromTempFileByName(tempFileName);
            FileModel file = filePersistence.getFileByName(tempFileName);

            try {
                file.setContent(excelManagerAdapter.getDataFromExcelFileInMemory(dataInMemory));
                return file;
            } catch (Exception ex) {
                throw new FileProcessingException(tempFileName);
            }
        } catch (IOException e) {
            throw new FileRetrievalException(tempFileName);
        }
    }

    public void saveSiigoFormat(InputStream fis) throws IOException {

        byte[] dataInMemory = FileAdministrator.saveInMemory(fis);

        FileAdministrator.saveSiigoFormat(dataInMemory);

    }

    public void deleteTemporaryFile(String fileName) {
        filePersistence.deleteFile(fileName);
        FileAdministrator.deleteTempFileByName (fileName);
    }

    @Override
    public File processSiigoFormat(String tempFileName) throws IOException {

        /*byte[] dataInMemory = FileAdministrator.getDataInMemoryFromTempFileByName(tempFileName);

        List<List<String>> listOfListData = excelManagerAdapter.getDataFromExcelFileInMemory(dataInMemory);

        LocalDate fileSavedFortNightDate = filePersistence.getFileByName(tempFileName).getFortNightDate();

        List<Employee> employees = fileDataProcessor.extractEmployeeData(listOfListData,
                fileSavedFortNightDate.getYear(), fileSavedFortNightDate.getMonthValue(), fileSavedFortNightDate.getDayOfMonth(),
                ConstantsDomain.TimeFormat.MILITARY);

        dataInMemory = FileAdministrator.getSiigoFormat();

        dataInMemory = excelManagerAdapter.populateSiigtoFormat(employees, dataInMemory);

        return FileAdministrator.saveTemporaryFileFromInMemoryBytes( dataInMemory, "siigoPopulate-");*/

        return  new File("");

    }

    @Override
    public List<FileModel> getFiles() {
        return filePersistence.getFiles();
    }

    @Override
    public byte[] getTempFile(String fileName) throws IOException {
        return FileAdministrator.getDataInMemoryFromTempFileByName(fileName);
    }




}
