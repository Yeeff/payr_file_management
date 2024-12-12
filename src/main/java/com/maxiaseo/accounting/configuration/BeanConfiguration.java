package com.maxiaseo.accounting.configuration;

import com.maxiaseo.accounting.adapters.driven.apachepoi.adapter.ExcelManagerAdapter;
import com.maxiaseo.accounting.domain.api.IPayrollServicesPort;
import com.maxiaseo.accounting.domain.api.usecase.PayrollServices;
import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import com.maxiaseo.accounting.domain.util.file.FileDataProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IExelManagerPort getExcelManagerAdapter(){
        return new ExcelManagerAdapter();
    }

    @Bean
    public FileDataProcessor getFileDataProcessor(){
        return new FileDataProcessor();
    }

    @Bean
    public IPayrollServicesPort getPayrollServicePort(){
        return new PayrollServices(getExcelManagerAdapter() ,getFileDataProcessor());
    }

}
