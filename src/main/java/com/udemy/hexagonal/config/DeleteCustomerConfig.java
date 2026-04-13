package com.udemy.hexagonal.config;

import com.udemy.hexagonal.application.core.usecase.DeleteCustomerUseCase;
import com.udemy.hexagonal.application.ports.in.DeleteCustomerInputPort;
import com.udemy.hexagonal.application.ports.out.DeleteCustomerOutputPort;
import com.udemy.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteCustomerConfig {
    @Bean
    public DeleteCustomerInputPort deleteCustomerInputPort(DeleteCustomerOutputPort deleteCustomerOutputPort, FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        return new DeleteCustomerUseCase(deleteCustomerOutputPort, findCustomerByIdOutputPort);
    }
}
