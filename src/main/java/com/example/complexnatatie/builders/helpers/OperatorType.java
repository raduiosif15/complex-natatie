package com.example.complexnatatie.builders.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OperatorType {

    private String name;

    // DO NOT REMOVE "ROLE_" KEYWORD, because of defaultRolePrefix in SecurityExpressionRoot
    public static final OperatorType ROLE_ADMIN = new OperatorType("ROLE_ADMIN");
    public static final OperatorType ROLE_CASHIER = new OperatorType("ROLE_CASHIER");
    public static final OperatorType ROLE_PORTER = new OperatorType("ROLE_PORTER");

    public static OperatorType[] values = new OperatorType[]{
            ROLE_ADMIN,
            ROLE_CASHIER,
            ROLE_PORTER,
    };
}
