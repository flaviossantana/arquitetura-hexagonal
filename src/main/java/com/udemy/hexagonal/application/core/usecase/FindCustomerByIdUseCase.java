package com.udemy.hexagonal.application.core.usecase;

import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.udemy.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FindCustomerByIdUseCase implements FindCustomerByIdInputPort {

    private final FindCustomerByIdOutputPort findCustomerByIdOutputPort;

    public FindCustomerByIdUseCase(FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        this.findCustomerByIdOutputPort = findCustomerByIdOutputPort;
    }

    @Override
    public Customer find(String id) {
        log.info("Find customer by id: {}", id);
        return findCustomerByIdOutputPort
                .find(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

}
