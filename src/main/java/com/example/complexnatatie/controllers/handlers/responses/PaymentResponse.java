package com.example.complexnatatie.controllers.handlers.responses;

import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.dtos.SubscriptionDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentResponse {

    private SubscriptionDTO subscription;

    private PaymentDTO payment;

    private boolean subscriptionCreated;

}
