package com.udemy.hexagonal.adapters.in.controller.mapper;

import com.udemy.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.udemy.hexagonal.adapters.in.controller.response.CustomerResponse;
import com.udemy.hexagonal.application.core.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "cpfValid", ignore = true)
    Customer toCustomer(CustomerRequest customerRequest);

    @Mapping(source = "address", target = "addressResponse")
    CustomerResponse toCustomerRequest(Customer customer);

}
