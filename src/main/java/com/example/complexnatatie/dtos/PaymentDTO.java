package com.example.complexnatatie.dtos;

import com.example.complexnatatie.builders.helpers.PaymentType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDTO {

    private int id;

    private Date date;

    private double value;

    private String description;

    private PaymentType type;

    private int customerId;

}
