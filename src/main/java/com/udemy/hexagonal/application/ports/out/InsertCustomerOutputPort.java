package com.udemy.hexagonal.application.ports.out;

import com.udemy.hexagonal.application.core.domain.Customer;

public interface InsertCustomerOutputPort {
    void insert(Customer customer);
}
