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
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .customerId(subscription.getCustomerId())
                .build();
    }

    public static List<SubscriptionDTO> fromEntities(List<Subscription> subscriptions) {
        //noinspection ComparatorMethodParameterNotUsed
        return subscriptions
                .stream()
                .map(SubscriptionBuilder::fromEntity)
                .sorted((s1, s2) -> s1.getStartDate().after(s2.getEndDate()) ? -1 : 1)
                .collect(Collectors.toList());
    }

}
