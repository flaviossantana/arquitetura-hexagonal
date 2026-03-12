package com.udemy.hexagonal.adapters.out.client;

import com.udemy.hexagonal.adapters.out.client.response.AddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "find-address-by-zip-code-client",
        url = "${hexagonal.client.address.url}")
public interface FindAddresByZipCodeClient {

    @GetMapping("/{zipCode}")
    AddressResponse find(@PathVariable("zipCode") String zipCode);
}
