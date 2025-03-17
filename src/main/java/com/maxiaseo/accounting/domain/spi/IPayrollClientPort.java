package com.maxiaseo.accounting.domain.spi;

import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.model.FileModel;

import java.util.List;

public interface IPayrollClientPort {
    List<Employee> extractPayrollDataFromSchedules( FileModel fileData);
}
