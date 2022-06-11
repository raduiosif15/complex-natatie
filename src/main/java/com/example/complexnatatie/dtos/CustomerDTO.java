package com.example.complexnatatie.dtos;

import com.example.complexnatatie.entities.Customer;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CustomerDTO {

    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String cnp;

    public static CustomerDTO fromCustomer(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .cnp(customer.getCnp())
                .build();
    }

    public static List<CustomerDTO> fromCustomers(List<Customer> customers) {
        return customers.stream().map(CustomerDTO::fromCustomer).collect(Collectors.toList());
    }


}
