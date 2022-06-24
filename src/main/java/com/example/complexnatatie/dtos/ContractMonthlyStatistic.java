package com.example.complexnatatie.dtos;

import com.example.complexnatatie.builders.helpers.CustomerType;
import lombok.*;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ContractMonthlyStatistic {

    private BigInteger count;

    private CustomerType type;

    private String month;

}
