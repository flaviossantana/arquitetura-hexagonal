package com.udemy.hexagonal.adapters.out;

import com.udemy.hexagonal.adapters.out.repository.CustomerRepository;
import com.udemy.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class FindCustomerByIdAdapter implements FindCustomerByIdOutputPort {

    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    public CustomerEntityMapper customerEntityMapper;

    @Override
    public Optional<Customer> find(String id) {
        log.info("Find customer by id: {}", id);
        return customerRepository
                .findById(id)
                .map(customerEntityMapper::toCustomer);
    }
}
