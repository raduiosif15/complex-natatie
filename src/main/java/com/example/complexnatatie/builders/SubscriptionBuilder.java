package com.example.complexnatatie.builders;

import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.entities.Subscription;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class SubscriptionBuilder {

    public static SubscriptionDTO fromEntity(Subscription subscription) {
        return SubscriptionDTO.builder()
                .id(subscription.getId())
                .valid(subscription.isValid())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .customerId(subscription.getCustomerId())
                .build();
    }

    public static List<SubscriptionDTO> fromEntities(List<Subscription> subscriptions) {
        return subscriptions.stream().map(SubscriptionBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Subscription fromDTO(SubscriptionDTO subscriptionDTO) {
        return Subscription.builder()
                .id(subscriptionDTO.getId())
                .valid(subscriptionDTO.isValid())
                .startDate(subscriptionDTO.getStartDate())
                .endDate(subscriptionDTO.getEndDate())
                .customerId(subscriptionDTO.getCustomerId())
                .build();
    }
}
