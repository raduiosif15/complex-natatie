package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.entities.Payment;
import com.example.complexnatatie.entities.PaymentCash;
import com.example.complexnatatie.entities.PaymentPos;
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

        System.out.println("payment type: " + paymentDTO.getType().getName());
        System.out.println("payment equals: " + paymentDTO.getType().equals(PaymentType.POS));

        if (paymentDTO.getType().getName().equals(PaymentType.POS.getName())) {

            System.out.println("payment type: " + paymentDTO.getType().getName());
            return new PaymentPos(
                    paymentDTO.getId(),
                    paymentDTO.getDate(),
                    paymentDTO.getValue(),
                    paymentDTO.getDescription(),
                    paymentDTO.getType().getName(),
                    paymentDTO.getCustomerId()
            );

        }


        return new PaymentCash(
                paymentDTO.getId(),
                paymentDTO.getDate(),
                paymentDTO.getValue(),
                paymentDTO.getDescription(),
                paymentDTO.getType().getName(),
                paymentDTO.getCustomerId()
        );
    }

}
