package com.udemy.hexagonal.adapters.in.controller;

import com.udemy.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.udemy.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.udemy.hexagonal.adapters.in.controller.response.CustomerResponse;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.udemy.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.udemy.hexagonal.application.ports.in.UpdateCustomerInputPort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CustomerRequest customerRequest) {

        Customer customer = customerMapper.toCustomer(customerRequest);
        insertCustomerInputPort.insert(customer, customerRequest.getZipCode());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody CustomerRequest customerRequest) {

        Customer customer = customerMapper.toCustomer(customerRequest);
        customer.setId(id);

        updateCustomerInputPort.updateCustomer(customer, customerRequest.getZipCode());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable String id) {
        return ResponseEntity
                .ok(customerMapper.toCustomerRequest(
                        findCustomerByIdUseCase.find(id))
                );
    }

}
