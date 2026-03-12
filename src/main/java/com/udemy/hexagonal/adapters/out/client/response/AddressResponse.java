package com.udemy.hexagonal.adapters.out.client.response;

import lombok.Data;

@Data
public class AddressResponse {
    private String city;
    private String state;
    private String street;
}
