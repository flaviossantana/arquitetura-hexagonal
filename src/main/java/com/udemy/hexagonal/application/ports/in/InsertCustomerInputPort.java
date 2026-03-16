package com.udemy.hexagonal.application.ports.in;

import com.udemy.hexagonal.application.core.domain.Customer;

public interface InsertCustomerInputPort {
    void insert(Customer customer, String zipCode);
}
