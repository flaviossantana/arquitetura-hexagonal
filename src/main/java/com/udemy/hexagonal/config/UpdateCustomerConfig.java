package com.udemy.hexagonal.config;

import com.udemy.hexagonal.application.core.usecase.UpdateCustomerUseCase;
import com.udemy.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.udemy.hexagonal.application.ports.in.UpdateCustomerInputPort;
import com.udemy.hexagonal.application.ports.out.FindAdressByZipCodeOutputPort;
import com.udemy.hexagonal.application.ports.out.UpdateCustomerOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UpdateCustomerConfig {
    @Bean
    public UpdateCustomerInputPort updateCustomerInputPort(UpdateCustomerOutputPort updateCustomerOutputPort, FindCustomerByIdInputPort findCustomerByIdInputPort, FindAdressByZipCodeOutputPort findAdressByZipCodeOutputPort) {
        return new UpdateCustomerUseCase(updateCustomerOutputPort, findCustomerByIdInputPort, findAdressByZipCodeOutputPort);
    }
}
