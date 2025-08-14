package com.horizonx.file_services.adapters.driven.feign.adapter;

import com.horizonx.file_services.adapters.driven.feign.client.IPayrollClientServices;
import com.horizonx.file_services.adapters.driven.feign.dto.EmployeeClientResponseDto;
import com.horizonx.file_services.adapters.driven.feign.dto.FileClientRequestDto;
import com.horizonx.file_services.adapters.driven.feign.mapper.IEmployeeClientResponseMapper;
import com.horizonx.file_services.adapters.driven.feign.mapper.IFileClientRequestMapper;
import com.horizonx.file_services.domain.model.Employee;
import com.horizonx.file_services.domain.model.FileModel;
import com.horizonx.file_services.domain.spi.IPayrollClientPort;

import java.util.List;

public class PayrollClientServicesAdapter implements IPayrollClientPort {

    IPayrollClientServices payrollClientSerivces;
    IFileClientRequestMapper fileResponseClientMapper;
    IEmployeeClientResponseMapper employeeClientResponseMapper;

    public PayrollClientServicesAdapter(IPayrollClientServices payrollClientSerivces, IFileClientRequestMapper fileResponseClientMapper, IEmployeeClientResponseMapper employeeClientResponseMapper) {
        this.payrollClientSerivces = payrollClientSerivces;
        this.fileResponseClientMapper = fileResponseClientMapper;
        this.employeeClientResponseMapper = employeeClientResponseMapper;
    }

    public List<Employee> extractPayrollDataFromSchedules(FileModel fileData) {

        FileClientRequestDto clientDto =  fileResponseClientMapper.toFileDto(fileData);

        List<EmployeeClientResponseDto> employeeClientResponse = payrollClientSerivces.extractPayrollDataFromSchedules(
                clientDto
        );

        List<Employee> list =  employeeClientResponseMapper.toListModel(
                employeeClientResponse
        );

        return list;
    }

}
