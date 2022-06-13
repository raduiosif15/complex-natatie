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

    private int number;

    private Date payDate;

    private double payValue;

    private String description;

    private PaymentType payType;

    private int customerId;


}
