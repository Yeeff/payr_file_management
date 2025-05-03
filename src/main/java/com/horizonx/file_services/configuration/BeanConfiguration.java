package com.horizonx.file_services.configuration;

import com.horizonx.file_services.adapters.driven.apachepoi.adapter.ExcelManagerAdapter;
import com.horizonx.file_services.adapters.driven.feign.adapter.PayrollClientServicesAdapter;
import com.horizonx.file_services.adapters.driven.feign.client.IPayrollClientServices;
import com.horizonx.file_services.adapters.driven.feign.mapper.IEmployeeClientResponseMapper;
import com.horizonx.file_services.adapters.driven.feign.mapper.IFileClientRequestMapper;
import com.horizonx.file_services.adapters.driven.jpa.mysql.adapter.FilePersistenceAdapterImpl;
import com.horizonx.file_services.adapters.driven.jpa.mysql.mapper.IFileEntityMapper;
import com.horizonx.file_services.adapters.driven.jpa.mysql.repository.FileRepository;
import com.horizonx.file_services.domain.api.IFileServicesPort;
import com.horizonx.file_services.domain.api.usecase.FileServicesAdapter;
import com.horizonx.file_services.domain.spi.IExelManagerPort;
import com.horizonx.file_services.domain.spi.IFilePersistencePort;
import com.horizonx.file_services.domain.service.file.FileDataProcessor;
import com.horizonx.file_services.domain.spi.IPayrollClientPort;
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
