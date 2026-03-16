package com.udemy.hexagonal.application.core.usecase;

import com.udemy.hexagonal.application.core.domain.Address;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.udemy.hexagonal.application.ports.out.FindAdressByZipCodeOutputPort;
import com.udemy.hexagonal.application.ports.out.InsertCustomerOutputPort;

public class InsertCustomerUseCase implements InsertCustomerInputPort {

    private final FindAdressByZipCodeOutputPort findAdressByZipCodeOutputPort;
    private final InsertCustomerOutputPort insertCustomerOutputPort;

    public InsertCustomerUseCase(FindAdressByZipCodeOutputPort findAdressByZipCodeOutputPort, InsertCustomerOutputPort insertCustomerOutputPort) {
        this.findAdressByZipCodeOutputPort = findAdressByZipCodeOutputPort;
        this.insertCustomerOutputPort = insertCustomerOutputPort;
    }

    @Override
    public void insert(Customer customer, String zipCode) {
        Address address = findAdressByZipCodeOutputPort.find(zipCode);
        customer.setAddress(address);
        insertCustomerOutputPort.insert(customer);
    }

}
