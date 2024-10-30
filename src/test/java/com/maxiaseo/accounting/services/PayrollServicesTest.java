package com.maxiaseo.accounting.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

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