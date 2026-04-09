package com.udemy.hexagonal.application.ports.in;

import com.udemy.hexagonal.application.core.domain.Customer;

public interface UpdateCustomerInputPort {
    void updateCustomer(Customer customer, String zipCode);
}
