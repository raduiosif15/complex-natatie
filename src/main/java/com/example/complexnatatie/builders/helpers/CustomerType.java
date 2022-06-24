package com.example.complexnatatie.builders.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomerType {

    private String name;

    public static final CustomerType ROLE_UTCN_STUDENT = new CustomerType("ROLE_UTCN_STUDENT");
    public static final CustomerType OTHER_STUDENT = new CustomerType("OTHER_STUDENT");
    public static final CustomerType ROLE_UTCN_EMPLOYEE = new CustomerType("ROLE_UTCN_EMPLOYEE");
    public static final CustomerType UTCN_RETIRED = new CustomerType("UTCN_RETIRED");
    public static final CustomerType UTCN_FAMILY = new CustomerType("UTCN_FAMILY");
    public static final CustomerType PERSON_WITH_DISABILITIES = new CustomerType("PERSON_WITH_DISABILITIES");
    public static final CustomerType ADULT = new CustomerType("ADULT");

    public static CustomerType[] values = new CustomerType[]{
            ROLE_UTCN_STUDENT,
            OTHER_STUDENT,
            ROLE_UTCN_EMPLOYEE,
            UTCN_RETIRED,
            UTCN_FAMILY,
            PERSON_WITH_DISABILITIES,
            ADULT
    };
}
