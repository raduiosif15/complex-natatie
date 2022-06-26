package com.example.complexnatatie.dtos;

import com.example.complexnatatie.builders.helpers.CustomerType;
import com.example.complexnatatie.builders.helpers.PaymentType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentMonthlyStatistic {

    private double value;

    private CustomerType customerType;

    private PaymentType paymentType;

    private String month;

}
