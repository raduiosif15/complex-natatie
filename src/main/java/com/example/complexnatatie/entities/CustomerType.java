package com.example.complexnatatie.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomerType {

    private String name;

    public static final CustomerType UTCN_STUDENT = new CustomerType("UTCN_STUDENT");
    public static final CustomerType OTHER_STUDENT = new CustomerType("OTHER_STUDENT");
    public static final CustomerType UTCN_EMPLOYEE = new CustomerType("UTCN_EMPLOYEE");
    public static final CustomerType UTCN_RETIRED = new CustomerType("UTCN_RETIRED");
    public static final CustomerType UTCN_FAMILY = new CustomerType("UTCN_FAMILY");
    public static final CustomerType PERSONS_WITH_DISABILITIES = new CustomerType("PERSONS_WITH_DISABILITIES");
    public static final CustomerType ADULTS = new CustomerType("ADULTS");

    public static CustomerType[] values = new CustomerType[]{
            UTCN_STUDENT,
            OTHER_STUDENT,
            UTCN_EMPLOYEE,
            UTCN_RETIRED,
            UTCN_FAMILY,
            PERSONS_WITH_DISABILITIES,
            ADULTS
    };
}
