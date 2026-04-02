package com.udemy.hexagonal.application.ports.in;

import com.udemy.hexagonal.application.core.domain.Customer;

public interface FindCustomerByIdInputPort {
    Customer find(String id);
}
