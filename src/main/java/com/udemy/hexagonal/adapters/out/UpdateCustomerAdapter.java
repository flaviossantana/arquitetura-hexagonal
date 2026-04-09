package com.udemy.hexagonal.adapters.out;
import com.udemy.hexagonal.adapters.out.repository.CustomerRepository;
import com.udemy.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.udemy.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.out.UpdateCustomerOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateCustomerAdapter implements UpdateCustomerOutputPort {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerEntityMapper customerEntityMapper;

    @Override
    public void update(Customer customer) {
        CustomerEntity customerEntity = customerEntityMapper.toEntity(customer);
        customerRepository.save(customerEntity);
    }
}
