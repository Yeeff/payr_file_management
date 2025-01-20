package com.maxiaseo.accounting.domain.api;

import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.model.FileModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IPayrollServicesPort {
    List<Employee> handleFileUpload(String tempFileName) throws IOException;

    FileModel saveFile(InputStream fis, Integer year, Integer month, Integer day) throws IOException;

    void deleteTemporaryFile(String fileName);

    File processSiigoFormat(String tempFileName) throws IOException;

    List<FileModel> getFiles();

    byte[] getTempFile(String fileName) throws IOException;

}
