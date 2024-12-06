package com.maxiaseo.accounting.domain.spi;

import com.maxiaseo.accounting.domain.model.Employee;

import java.io.IOException;
import java.util.List;

public interface IExelManagerPort {

    List<List<String>> getDataFromExcelFileInMemory(byte[] inMemoryFile)throws IOException;

    byte[] updateEmployeeDataInExcel(byte[] excelData, List<Employee> employees) throws IOException;
}
