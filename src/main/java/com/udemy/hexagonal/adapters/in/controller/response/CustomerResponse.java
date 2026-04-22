package com.udemy.hexagonal.adapters.in.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private String id;
    private String name;
    private String cpf;
    private Boolean isCpfValid;
    private AddressResponse addressResponse;
}
