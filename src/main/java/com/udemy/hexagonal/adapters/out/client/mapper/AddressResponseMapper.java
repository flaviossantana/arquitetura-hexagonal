package com.udemy.hexagonal.adapters.out.client.mapper;

import com.udemy.hexagonal.adapters.out.client.response.AddressResponse;
import com.udemy.hexagonal.application.core.domain.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressResponseMapper {

    Address toAddress(AddressResponse addressResponse);
}
