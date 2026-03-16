package com.udemy.hexagonal.application.ports.out;

import com.udemy.hexagonal.application.core.domain.Customer;

import java.util.Optional;

public interface FindCustomerByIdOutputPort {
    Optional<Customer> find(String id);
}
