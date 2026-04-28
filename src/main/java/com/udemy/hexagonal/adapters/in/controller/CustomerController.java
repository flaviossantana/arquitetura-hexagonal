package com.udemy.hexagonal.adapters.in.controller;

import com.udemy.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.udemy.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.udemy.hexagonal.adapters.in.controller.response.CustomerResponse;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.in.DeleteCustomerInputPort;
import com.udemy.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.udemy.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.udemy.hexagonal.application.ports.in.UpdateCustomerInputPort;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @Autowired
    private InsertCustomerInputPort insertCustomerInputPort;

    @Autowired
    private FindCustomerByIdInputPort findCustomerByIdUseCase;

    @Autowired
    private UpdateCustomerInputPort updateCustomerInputPort;

    @Autowired
    private DeleteCustomerInputPort deleteCustomerInputPort;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping("{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable String id) {
        return executeCustomerCall(() -> {
            log.info("Find customer by id: {}", id);
            Customer customer = findCustomerByIdUseCase.find(id);
            log.info("Customer found: {}", customer);
            return ResponseEntity
                    .ok(customerMapper.toCustomerRequest(customer));
        });
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CustomerRequest customerRequest) {
        return executeCustomerCall(() -> {
            log.info("Insert customer: {}", customerRequest);

            Customer customer = customerMapper.toCustomer(customerRequest);
            log.info("Customer mapped: {}", customer);

            insertCustomerInputPort.insert(customer, customerRequest.getZipCode());
            log.info("Customer inserted");

            return ResponseEntity.ok().build();
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody CustomerRequest customerRequest) {
        return executeCustomerCall(() -> {
            log.info("Update customer: {}", customerRequest);

            Customer customer = customerMapper.toCustomer(customerRequest);
            customer.setId(id);
            log.info("CustomerRequest mapped Customer: {}", customer);

            updateCustomerInputPort.updateCustomer(customer, customerRequest.getZipCode());
            log.info("Customer updated");

            return ResponseEntity.noContent().build();
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return executeCustomerCall(() -> {
            log.info("Delete customer by id: {}", id);
            deleteCustomerInputPort.delete(id);
            log.info("Customer deleted");
            return ResponseEntity.noContent().build();
        });
    }

    private <T> T executeCustomerCall(Supplier<T> metodo){
        try {
            MDC.put("correlationID", UUID.randomUUID().toString());
            return metodo.get();
        }finally {
            MDC.remove("correlationID");
        }
    }

}
