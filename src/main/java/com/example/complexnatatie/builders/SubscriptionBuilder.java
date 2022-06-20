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
                .contractId(subscription.getContractId())
                .build();
    }

    public static List<SubscriptionDTO> fromEntities(List<Subscription> subscriptions) {
        // dates cannot be equals
        //noinspection ComparatorMethodParameterNotUsed
        return subscriptions
                .stream()
                .map(SubscriptionBuilder::fromEntity)
                .sorted((s1, s2) -> s1.getStartDate().after(s2.getStartDate()) ? -1 : 1)
                .collect(Collectors.toList());
    }

    public static Subscription fromDTO(SubscriptionDTO subscriptionDTO) {
        return Subscription.builder()
                .id(subscriptionDTO.getId())
                .startDate(subscriptionDTO.getStartDate())
                .endDate(subscriptionDTO.getEndDate())
                .contractId(subscriptionDTO.getContractId())
                .build();
    }
}
