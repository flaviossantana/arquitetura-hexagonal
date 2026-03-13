package com.udemy.hexagonal.adapters.out.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customer")
public class CostumerEntity {

    @Id
    private String id;
    private String name;
    private String cpf;
    private AddressEntity address;
    private Boolean isCpfValid;
}
