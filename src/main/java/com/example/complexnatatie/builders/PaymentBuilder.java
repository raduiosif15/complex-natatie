package com.example.complexnatatie.builders;

import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.entities.Payment;
import com.example.complexnatatie.builders.helpers.PaymentType;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PaymentBuilder {

    public static PaymentDTO fromEntity(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .date(payment.getDate())
                .value(payment.getValue())
                .type(new PaymentType(payment.getType()))
                .description(payment.getDescription())
                .customerId(payment.getCustomerId())

                .build();
    }

    public static List<PaymentDTO> fromEntities(List<Payment> payments) {
        return payments.stream().map(PaymentBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Payment fromDTO(PaymentDTO paymentDTO) {
        return Payment.builder()
                .id(paymentDTO.getId())
                .date(paymentDTO.getDate())
                .value(paymentDTO.getValue())
                .type(paymentDTO.getType().getName())
                .description(paymentDTO.getDescription())
                .customerId(paymentDTO.getCustomerId())
                .build();
    }
}
