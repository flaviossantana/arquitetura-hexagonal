package com.udemy.hexagonal.adapters.out;

import com.udemy.hexagonal.adapters.out.repository.CustomerRepository;
import com.udemy.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.udemy.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.out.InsertCustomerOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InsertCustomerAdapter implements InsertCustomerOutputPort {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerEntityMapper customerEntityMapper;

    @Override
    public void insert(Customer customer) {
        log.info("Inserting customer: {}", customer);
        CustomerEntity customerEntity = customerEntityMapper.toEntity(customer);
        log.info("Customer mapped: {}", customerEntity);
        customerRepository.insert(customerEntity);
        log.info("Customer inserted with id: {}", customerEntity.getId());
    }
}
