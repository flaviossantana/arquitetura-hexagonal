package com.udemy.hexagonal.application.core.usecase;

import com.udemy.hexagonal.application.ports.in.DeleteCustomerInputPort;
import com.udemy.hexagonal.application.ports.out.DeleteCustomerOutputPort;
import com.udemy.hexagonal.application.ports.out.FindCustomerByIdOutputPort;

public class DeleteCustomerUseCase implements DeleteCustomerInputPort {

    private final DeleteCustomerOutputPort deleteCustomerOutputPort;

    private final FindCustomerByIdOutputPort findCustomerByIdOutputPort;

    public DeleteCustomerUseCase(DeleteCustomerOutputPort deleteCustomerOutputPort, FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        this.deleteCustomerOutputPort = deleteCustomerOutputPort;
        this.findCustomerByIdOutputPort = findCustomerByIdOutputPort;
    }

    @Override
    public void delete(String id) {
        findCustomerByIdOutputPort
                .find(id)
                .ifPresent( customer ->  deleteCustomerOutputPort.delete(customer.getId()));
    }
}
