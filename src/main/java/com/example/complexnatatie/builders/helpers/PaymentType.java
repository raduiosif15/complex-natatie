package com.example.complexnatatie.builders.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentType {

    private String name;

    public static final PaymentType CASH = new PaymentType("CASH");
    public static final PaymentType POS = new PaymentType("POS");

    public static PaymentType[] values = new PaymentType[]{
            CASH,
            POS,
    };
}
