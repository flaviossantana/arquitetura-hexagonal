package com.udemy.hexagonal.config;

import com.udemy.hexagonal.application.core.usecase.InsertCustomerUseCase;
import com.udemy.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.udemy.hexagonal.application.ports.out.FindAdressByZipCodeOutputPort;
import com.udemy.hexagonal.application.ports.out.InsertCustomerOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public InsertCustomerInputPort insertCustomerInputPort(
            FindAdressByZipCodeOutputPort findAdressByZipCodeOutputPort,
            InsertCustomerOutputPort insertCustomerOutputPort) {
        return new InsertCustomerUseCase(findAdressByZipCodeOutputPort, insertCustomerOutputPort);
    }

}
