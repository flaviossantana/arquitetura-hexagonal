package com.udemy.hexagonal.adapters.out.repository.mapper;


import com.udemy.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.udemy.hexagonal.application.core.domain.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

    CustomerEntity toEntity(Customer customer);
}
