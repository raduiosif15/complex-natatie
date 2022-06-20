package com.example.complexnatatie.dtos;

import com.example.complexnatatie.builders.helpers.CustomerType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ContractDTO {

    private int id;

    private Date startDate;

    private Date endDate;

    private double monthly;

    private double total;

    private CustomerType customerType;

    private int customerId;

}
