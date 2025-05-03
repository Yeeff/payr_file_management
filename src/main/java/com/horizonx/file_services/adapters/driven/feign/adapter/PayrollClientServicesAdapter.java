package com.horizonx.file_services.adapters.driven.feign.adapter;

import com.horizonx.file_services.adapters.driven.feign.client.IPayrollClientServices;
import com.horizonx.file_services.adapters.driven.feign.mapper.IEmployeeClientResponseMapper;
import com.horizonx.file_services.adapters.driven.feign.mapper.IFileClientRequestMapper;
import com.horizonx.file_services.domain.model.Employee;
import com.horizonx.file_services.domain.model.FileModel;
import com.horizonx.file_services.domain.spi.IPayrollClientPort;

import java.util.List;

public class PayrollClientServicesAdapter implements IPayrollClientPort {

    IPayrollClientServices payrollClientSerivces;
    IFileClientRequestMapper fileResponseClientMapper;
    IEmployeeClientResponseMapper employeeClientResponseDto;

    public PayrollClientServicesAdapter(IPayrollClientServices payrollClientSerivces, IFileClientRequestMapper fileResponseClientMapper, IEmployeeClientResponseMapper employeeClientResponseDto) {
        this.payrollClientSerivces = payrollClientSerivces;
        this.fileResponseClientMapper = fileResponseClientMapper;
        this.employeeClientResponseDto = employeeClientResponseDto;
    }

    public List<Employee> extractPayrollDataFromSchedules(FileModel fileData) {
        return employeeClientResponseDto.toListModel(
                payrollClientSerivces.extractPayrollDataFromSchedules(
                        fileResponseClientMapper.toFileDto(fileData)
                )
        );
    }

}
