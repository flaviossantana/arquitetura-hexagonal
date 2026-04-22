package com.udemy.hexagonal.adapters.out;

import com.udemy.hexagonal.adapters.out.repository.CustomerRepository;
import com.udemy.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.udemy.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.out.UpdateCustomerOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateCustomerAdapter implements UpdateCustomerOutputPort {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerEntityMapper customerEntityMapper;

    @Override
    public void update(Customer customer) {
        log.info("Updating customer: {}", customer);
        CustomerEntity customerEntity = customerEntityMapper.toEntity(customer);
        log.info("Customer mapped: {}", customerEntity);
        customerRepository.save(customerEntity);
        log.info("Customer updated");
    }
}
