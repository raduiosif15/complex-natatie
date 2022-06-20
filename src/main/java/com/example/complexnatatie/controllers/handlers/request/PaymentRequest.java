package com.example.complexnatatie.controllers.handlers.request;

import com.example.complexnatatie.builders.helpers.PaymentType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentRequest {

    private int months;

    private int customerId;

    private String description;

    private PaymentType type;

}
