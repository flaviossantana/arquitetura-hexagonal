package com.udemy.hexagonal.adapters.in.controller.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private String id;
    private String name;
    private String cpf;
    private Boolean isCpfValid;
    private AddressResponse addressResponse;
}
