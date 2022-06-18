package com.example.complexnatatie.controllers.handlers.responses;

import com.example.complexnatatie.dtos.SubscriptionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionResponse {
    private SubscriptionDTO subscription;
}
