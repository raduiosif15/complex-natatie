package com.example.complexnatatie.builders;

import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.entities.Subscription;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class SubscriptionBuilder {

    public static SubscriptionDTO fromEntity(Subscription contract) {
        return SubscriptionDTO.builder()
                .id(contract.getId())
                .valid(contract.isValid())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .customerId(contract.getCustomerId())
                .build();
    }

    public static List<SubscriptionDTO> fromEntities(List<Subscription> contracts) {
        return contracts.stream().map(SubscriptionBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Subscription fromDTO(SubscriptionDTO contractDTO) {
        return Subscription.builder()
                .id(contractDTO.getId())
                .valid(contractDTO.isValid())
                .startDate(contractDTO.getStartDate())
                .endDate(contractDTO.getEndDate())
                .customerId(contractDTO.getCustomerId())
                .build();
    }
}
