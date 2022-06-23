package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.dtos.PaymentForReport;
import com.example.complexnatatie.entities.Contract;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.entities.Payment;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    public static List<PaymentForReport> fromObjects(List<Object[]> objects) {
        final List<PaymentForReport> paymentForReportList = new ArrayList<>();

        for (Object[] objDetails : objects) {

            final PaymentForReport paymentForReport = new PaymentForReport();

            paymentForReport.setPayment(PaymentBuilder.fromEntity((Payment) (objDetails[0])));
            paymentForReport.setCustomer(CustomerBuilder.fromEntity((Customer) (objDetails[1])));
            paymentForReport.setContract(ContractBuilder.fromEntity((Contract) (objDetails[2])));

            paymentForReportList.add(paymentForReport);

        }

        //noinspection ComparatorMethodParameterNotUsed
        return paymentForReportList
                .stream()
                .sorted((p1, p2) -> p1.getPayment().getDate().after(p2.getPayment().getDate()) ? 1 : -1)
                .toList();
    }

}
