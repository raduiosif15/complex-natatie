package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.CustomerType;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.dtos.PaymentForReport;
import com.example.complexnatatie.dtos.PaymentMonthlyStatistic;
import com.example.complexnatatie.entities.Contract;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.entities.Payment;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class PaymentBuilder {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static PaymentDTO fromEntity(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .date(payment.getDate())
                .value(Double.parseDouble(df.format(payment.getValue())))
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

    public static List<PaymentMonthlyStatistic> fromStatisticObjects(List<Object[]> objects, PaymentType paymentType) {
        final List<PaymentMonthlyStatistic> paymentForReportList = new ArrayList<>();

        for (Object[] objDetails : objects) {

            final PaymentMonthlyStatistic paymentMonthlyStatistic = new PaymentMonthlyStatistic();

            Double value = (double) objDetails[0];

            paymentMonthlyStatistic.setValue(Double.parseDouble(df.format(value)));
            paymentMonthlyStatistic.setCustomerType(new CustomerType((String) objDetails[1]));
            paymentMonthlyStatistic.setMonth((String) objDetails[2]);
            paymentMonthlyStatistic.setPaymentType(paymentType);

            paymentForReportList.add(paymentMonthlyStatistic);

        }

        return paymentForReportList;
    }

}
