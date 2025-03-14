package com.maxiaseo.accounting.adapters.driven.feign.mapper;

import com.maxiaseo.accounting.adapters.driven.feign.dto.EmployeeClientResponseDto;
import com.maxiaseo.accounting.domain.model.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IEmployeeClientResponseMapper {

    List<Employee> toListModel(List<EmployeeClientResponseDto> employeeClientResponseDtos);
}
