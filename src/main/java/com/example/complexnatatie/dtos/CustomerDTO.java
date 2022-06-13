package com.example.complexnatatie.dtos;

import com.example.complexnatatie.builders.helpers.CustomerType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerDTO {

    private int id;

    private String firstName;

    private String lastName;

    private String phone;

    private String cnp;

    private String utcnID;

    private CustomerType customerType;

}
