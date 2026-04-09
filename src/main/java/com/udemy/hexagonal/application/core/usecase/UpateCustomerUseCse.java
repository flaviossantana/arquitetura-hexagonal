package com.udemy.hexagonal.application.core.usecase;

import com.udemy.hexagonal.application.core.domain.Address;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.udemy.hexagonal.application.ports.in.UpdateCustomerInputPort;
import com.udemy.hexagonal.application.ports.out.FindAdressByZipCodeOutputPort;
import com.udemy.hexagonal.application.ports.out.UpdateCustomerOutputPort;

public class UpateCustomerUseCse implements UpdateCustomerInputPort {

    private final UpdateCustomerOutputPort updateCustomerOutputPort;
    private final FindCustomerByIdInputPort findCustomerByIdInputPort;
    private final FindAdressByZipCodeOutputPort findAdressByZipCodeOutputPort;

    public UpateCustomerUseCse(UpdateCustomerOutputPort updateCustomerOutputPort, FindCustomerByIdInputPort findCustomerByIdInputPort, FindAdressByZipCodeOutputPort findAdressByZipCodeOutputPort) {
        this.updateCustomerOutputPort = updateCustomerOutputPort;
        this.findCustomerByIdInputPort = findCustomerByIdInputPort;
        this.findAdressByZipCodeOutputPort = findAdressByZipCodeOutputPort;
    }

    @Override
    public void updateCustomer(Customer customer, String zipCode) {

        Customer customerID = findCustomerByIdInputPort.find(customer.getId());
        Address address = findAdressByZipCodeOutputPort.find(zipCode);

        customer.setId(customerID.getId());
        customer.setAddress(address);

        updateCustomerOutputPort.update(customer);

    }
}
