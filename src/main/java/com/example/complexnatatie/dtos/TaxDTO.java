package com.example.complexnatatie.dtos;

import com.example.complexnatatie.builders.helpers.CustomerType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TaxDTO {

    private int id;

    private String description;

    private CustomerType type;

    private double value;

}
