package com.udemy.hexagonal.adapters.out;

import com.udemy.hexagonal.adapters.out.client.FindAddresByZipCodeClient;
import com.udemy.hexagonal.adapters.out.client.mapper.AddressResponseMapper;
import com.udemy.hexagonal.adapters.out.client.response.AddressResponse;
import com.udemy.hexagonal.application.core.domain.Address;
import com.udemy.hexagonal.application.ports.out.FindAdressByZipCodeOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FindAddressZipCodeAdapter implements FindAdressByZipCodeOutputPort {

    @Autowired
    private FindAddresByZipCodeClient findAddresByZipCodeClient;

    @Autowired
    private AddressResponseMapper addressResponseMapper;

    @Override
    public Address find(String zipCode) {
        AddressResponse addressResponse = findAddresByZipCodeClient.find(zipCode);
        return addressResponseMapper.toAddress(addressResponse);
    }
}
