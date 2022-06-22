package com.example.complexnatatie.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentForReport {

    private PaymentDTO payment;

    private CustomerDTO customer;

    private ContractDTO contract;

}
