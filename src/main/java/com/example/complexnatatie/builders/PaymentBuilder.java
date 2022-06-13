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

                .number(payment.getNumber())
                .payDate(payment.getPayDate())
                .payValue(payment.getPayValue())
                .payType(new PaymentType(payment.getPayType()))
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
                .number(paymentDTO.getNumber())
                .payDate(paymentDTO.getPayDate())
                .payValue(paymentDTO.getPayValue())
                .payType(paymentDTO.getPayType().getName())
                .description(paymentDTO.getDescription())
                .customerId(paymentDTO.getCustomerId())
                .build();
    }
}
