package com.udemy.hexagonal.application.core.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerID) {
        super(String.format("Customer not found. ID %s", customerID) );
    }

}
