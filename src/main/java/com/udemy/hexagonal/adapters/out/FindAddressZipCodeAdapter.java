package com.udemy.hexagonal.adapters.out;

import com.udemy.hexagonal.adapters.out.client.FindAddresByZipCodeClient;
import com.udemy.hexagonal.adapters.out.client.mapper.AddressResponseMapper;
import com.udemy.hexagonal.adapters.out.client.response.AddressResponse;
import com.udemy.hexagonal.application.core.domain.Address;
import com.udemy.hexagonal.application.ports.out.FindAdressByZipCodeOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FindAddressZipCodeAdapter implements FindAdressByZipCodeOutputPort {

    @Autowired
    private FindAddresByZipCodeClient findAddresByZipCodeClient;

    @Autowired
    private AddressResponseMapper addressResponseMapper;

    @Override
    public Address find(String zipCode) {
        log.info("Finding address by zip code: {}", zipCode);
        AddressResponse addressResponse = findAddresByZipCodeClient.find(zipCode);
        log.info("Address found: {}", addressResponse);
        return addressResponseMapper.toAddress(addressResponse);
    }
}
