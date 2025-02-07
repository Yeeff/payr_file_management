package com.maxiaseo.accounting.configuration;

import com.maxiaseo.accounting.adapters.driven.apachepoi.adapter.ExcelManagerAdapter;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.adapter.FilePersistenceAdapterImpl;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.mapper.IFileEntityMapper;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.repository.FileRepository;
import com.maxiaseo.accounting.domain.api.IPayrollServicesPort;
import com.maxiaseo.accounting.domain.api.usecase.PayrollServices;
import com.maxiaseo.accounting.domain.spi.IExelManagerPort;
import com.maxiaseo.accounting.domain.spi.IFilePersistencePort;
import com.maxiaseo.accounting.domain.service.file.FileDataProcessor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class BeanConfiguration {

    private FileRepository fileRepository;
    private IFileEntityMapper fileEntityMapper;

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
        return new PayrollServices(getExcelManagerAdapter() ,getFileDataProcessor(), getFilePersistenceAdapter());
    }

    @Bean
    public IFilePersistencePort getFilePersistenceAdapter(){
        return new FilePersistenceAdapterImpl(fileRepository, fileEntityMapper);
    }

}
