package com.example.complexnatatie.builders;

import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.builders.helpers.CustomerType;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CustomerBuilder {
    public static CustomerDTO fromEntity(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .cnp(customer.getCnp())
                .utcnID(customer.getUtcnID())
                .type(new CustomerType(customer.getType()))
                .build();
    }

    public static List<CustomerDTO> fromEntities(List<Customer> customers) {
        return customers.stream().map(CustomerBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Customer fromDTO(CustomerDTO customerDTO) {
        return Customer.builder()
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .phone(customerDTO.getPhone())
                .cnp(customerDTO.getCnp())
                .utcnID(customerDTO.getUtcnID())
                .type(customerDTO.getType().getName())
                .build();
    }
}
