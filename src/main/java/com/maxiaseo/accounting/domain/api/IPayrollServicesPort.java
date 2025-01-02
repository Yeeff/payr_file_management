package com.maxiaseo.accounting.domain.api;

import com.maxiaseo.accounting.domain.model.Employee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IPayrollServicesPort {
    List<Employee> handleFileUpload(String tempFileName, Integer year, Integer month, Integer initDay) throws IOException;

    File saveFile(InputStream fis, Integer year, Integer month, Integer day) throws IOException;

    void deleteTemporaryFile(File tempFile);

    File processSiigoFormat(String tempFileName, Integer year, Integer month, Integer initDay) throws IOException;
}
