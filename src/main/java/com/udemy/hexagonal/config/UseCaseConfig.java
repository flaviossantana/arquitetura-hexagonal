package com.udemy.hexagonal.config;

import com.udemy.hexagonal.application.core.usecase.DeleteCustomerUseCase;
import com.udemy.hexagonal.application.core.usecase.FindCustomerByIdUseCase;
import com.udemy.hexagonal.application.core.usecase.InsertCustomerUseCase;
import com.udemy.hexagonal.application.core.usecase.UpdateCustomerUseCase;
import com.udemy.hexagonal.application.ports.in.DeleteCustomerInputPort;
import com.udemy.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.udemy.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.udemy.hexagonal.application.ports.in.UpdateCustomerInputPort;
import com.udemy.hexagonal.application.ports.out.*;
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

    @Bean
    public FindCustomerByIdInputPort findCustomerByIdInputPort(FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        return new FindCustomerByIdUseCase(findCustomerByIdOutputPort);
    }

    @Bean
    public UpdateCustomerInputPort updateCustomerInputPort(UpdateCustomerOutputPort updateCustomerOutputPort, FindCustomerByIdInputPort findCustomerByIdInputPort, FindAdressByZipCodeOutputPort findAdressByZipCodeOutputPort) {
        return new UpdateCustomerUseCase(updateCustomerOutputPort, findCustomerByIdInputPort, findAdressByZipCodeOutputPort);
    }

    @Bean
    public DeleteCustomerInputPort deleteCustomerInputPort(DeleteCustomerOutputPort deleteCustomerOutputPort) {
        return new DeleteCustomerUseCase(deleteCustomerOutputPort);
    }


}
