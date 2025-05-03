package com.horizonx.file_services.domain.spi;

import com.horizonx.file_services.domain.model.Employee;
import com.horizonx.file_services.domain.model.FileModel;

import java.util.List;

public interface IPayrollClientPort {
    List<Employee> extractPayrollDataFromSchedules( FileModel fileData);
}
