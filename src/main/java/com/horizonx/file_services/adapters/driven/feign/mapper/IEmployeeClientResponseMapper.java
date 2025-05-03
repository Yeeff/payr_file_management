package com.horizonx.file_services.adapters.driven.feign.mapper;

import com.horizonx.file_services.adapters.driven.feign.dto.EmployeeClientResponseDto;
import com.horizonx.file_services.domain.model.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IEmployeeClientResponseMapper {

    List<Employee> toListModel(List<EmployeeClientResponseDto> employeeClientResponseDtos);
}
