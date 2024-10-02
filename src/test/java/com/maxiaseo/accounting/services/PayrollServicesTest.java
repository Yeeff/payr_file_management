package com.maxiaseo.accounting.services;

import com.maxiaseo.accounting.domain.Surcharge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PayrollServicesTest {

    @InjectMocks
    PayrollServices payrollServices;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void parseDateTime(){

//        List<Surcharge> result = new ArrayList<>();
//
//        result = payrollServices.processSchedule("7 a 22", 15) ;
//
//        assertEquals(1, result.size());
    }

}