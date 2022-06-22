package com.example.complexnatatie.dtos;

import com.example.complexnatatie.entities.Customer;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentWithCustomer {

    private PaymentDTO payment;

    private Customer customer;

}
