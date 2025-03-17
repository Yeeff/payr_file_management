package com.maxiaseo.accounting.adapters.driven.feign.adapter;

import com.maxiaseo.accounting.adapters.driven.feign.client.IPayrollClientServices;
import com.maxiaseo.accounting.adapters.driven.feign.mapper.IEmployeeClientResponseMapper;
import com.maxiaseo.accounting.adapters.driven.feign.mapper.IFileClientRequestMapper;
import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.model.FileModel;
import com.maxiaseo.accounting.domain.spi.IPayrollClientPort;

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
