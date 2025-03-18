package com.maxiaseo.accounting.adapters.driven.feign.client;

import com.maxiaseo.accounting.adapters.driven.feign.dto.EmployeeClientResponseDto;
import com.maxiaseo.accounting.adapters.driven.feign.dto.FileClientRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "payroll-services", url = "${payroll-services.url}")
public interface IPayrollClientServices {

    @PostMapping("/process")
    List<EmployeeClientResponseDto> extractPayrollDataFromSchedules(@RequestBody FileClientRequestDto data);
}
