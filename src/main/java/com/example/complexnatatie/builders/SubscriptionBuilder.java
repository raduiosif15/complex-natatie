package com.example.complexnatatie.builders;

import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.entities.Subscription;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SubscriptionBuilder {

    public static SubscriptionDTO fromEntity(Subscription subscription) {
        return SubscriptionDTO.builder()
                .id(subscription.getId())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .customerId(subscription.getCustomerId())
                .build();
    }

}
