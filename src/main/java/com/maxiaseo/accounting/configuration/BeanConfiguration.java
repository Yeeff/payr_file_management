package com.maxiaseo.accounting.configuration;

import com.maxiaseo.accounting.adapters.driven.apachepoi.adapter.ExcelManagerAdapter;
import com.maxiaseo.accounting.adapters.driven.feign.adapter.PayrollClientServicesAdapter;
import com.maxiaseo.accounting.adapters.driven.feign.client.IPayrollClientServices;
import com.maxiaseo.accounting.adapters.driven.feign.mapper.IEmployeeClientResponseMapper;
import com.maxiaseo.accounting.adapters.driven.feign.mapper.IFileClientRequestMapper;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.adapter.FilePersistenceAdapterImpl;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.mapper.IFileEntityMapper;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.repository.FileRepository;
import com.maxiaseo.accounting.domain.api.IFileServicesPort;
import com.maxiaseo.accounting.domain.api.usecase.FileServicesAdapter;
import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import com.maxiaseo.accounting.domain.spi.IFilePersistencePort;
import com.maxiaseo.accounting.domain.service.file.FileDataProcessor;
import com.maxiaseo.accounting.domain.spi.IPayrollClientPort;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class BeanConfiguration {

    private FileRepository fileRepository;
    private IFileEntityMapper fileEntityMapper;
    private IPayrollClientServices payrollClientServices;
    private IFileClientRequestMapper fileResponseClientMapper;
    private IEmployeeClientResponseMapper employeeClientResponseMapper;

    @Bean
    public IFileServicesPort getPayrollServicePort(){
        return new FileServicesAdapter(getExcelManagerAdapter() ,
                getFileDataProcessor(),
                getFilePersistenceAdapter(),
                getPayrollClientServicesPort()
        );
    }

    @Bean
    public IExelManagerPort getExcelManagerAdapter(){
        return new ExcelManagerAdapter();
    }

    @Bean
    public FileDataProcessor getFileDataProcessor(){
        return new FileDataProcessor();
    }

    @Bean
    public IFilePersistencePort getFilePersistenceAdapter(){
        return new FilePersistenceAdapterImpl(fileRepository, fileEntityMapper);
    }
    @Bean
    public IPayrollClientPort getPayrollClientServicesPort(){
        return  new PayrollClientServicesAdapter(payrollClientServices, fileResponseClientMapper, employeeClientResponseMapper);
    }


}
