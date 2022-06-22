package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.dtos.PaymentWithCustomer;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.entities.Payment;
import com.example.complexnatatie.entities.PaymentCash;
import com.example.complexnatatie.entities.PaymentPos;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    public static List<PaymentWithCustomer> fromObjects(List<Object[]> objects) {
        final List<PaymentWithCustomer> paymentWithCustomerList = new ArrayList<>();

        for (Object[] objDetails : objects) {

            final PaymentWithCustomer paymentWithCustomer = new PaymentWithCustomer();

            paymentWithCustomer.setPayment(PaymentBuilder.fromEntity((Payment) (objDetails[0])));
            paymentWithCustomer.setCustomer(CustomerBuilder.fromEntity((Customer) (objDetails[1])));

            paymentWithCustomerList.add(paymentWithCustomer);

        }

        return paymentWithCustomerList;
    }

}
