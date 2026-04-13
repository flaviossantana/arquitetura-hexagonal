package com.udemy.hexagonal.config;

import com.udemy.hexagonal.application.core.usecase.FindCustomerByIdUseCase;
import com.udemy.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.udemy.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindCustomerByIdConfig {
    @Bean
    public FindCustomerByIdInputPort findCustomerByIdInputPort(FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        return new FindCustomerByIdUseCase(findCustomerByIdOutputPort);
    }
}
