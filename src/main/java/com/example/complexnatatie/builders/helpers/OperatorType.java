package com.example.complexnatatie.builders.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OperatorType {

    private String name;

    public static final OperatorType ADMIN = new OperatorType("ADMIN");
    public static final OperatorType CASHIER = new OperatorType("CASHIER");
    public static final OperatorType PORTER = new OperatorType("PORTER");

    public static OperatorType[] values = new OperatorType[]{
            ADMIN,
            CASHIER,
            PORTER,
    };
}
