package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.CustomerType;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.security.service.UserDetailsImpl;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
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
                .codeID(customer.getCodeID())
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
                .codeID(customerDTO.getCodeID())
                .type(customerDTO.getType().getName())
                .build();
    }

    public static UserDetailsImpl userDetailsBuilder(Customer customer) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(customer.getType()));

        return UserDetailsImpl.builder()
                .id(customer.getId())
                .utcnId(customer.getUtcnID())
                .type(customer.getType())
                .username(customer.getUtcnID())
                .password(customer.getPassword())
                .authorities(authorities)
                .build();
    }
}
