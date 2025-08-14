package com.horizonx.file_services.domain.spi;

import com.horizonx.file_services.domain.model.Employee;

import java.io.IOException;
import java.util.List;

public interface IExelManagerPort {

    List<List<String>> getDataFromExcelFileInMemory(byte[] inMemoryFile)throws IOException;

    byte[] updateEmployeeDataInExcel(byte[] excelData, List<Employee> employees) throws IOException;

    }
