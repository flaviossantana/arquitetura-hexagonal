package com.udemy.hexagonal.application.core.usecase;

import com.udemy.hexagonal.application.ports.in.DeleteCustomerInputPort;
import com.udemy.hexagonal.application.ports.out.DeleteCustomerOutputPort;

public class DeleteCustomerUseCase implements DeleteCustomerInputPort {

    private final DeleteCustomerOutputPort deleteCustomerOutputPort;

    public DeleteCustomerUseCase(DeleteCustomerOutputPort deleteCustomerOutputPort) {
        this.deleteCustomerOutputPort = deleteCustomerOutputPort;
    }

    @Override
    public void delete(String id) {
        deleteCustomerOutputPort.delete(id);
    }
}
