package com.udemy.hexagonal.adapters.out.client.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressResponse {
    private String city;
    private String state;
    private String street;
}
