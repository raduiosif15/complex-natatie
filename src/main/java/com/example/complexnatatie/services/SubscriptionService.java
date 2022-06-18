package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.SubscriptionBuilder;
import com.example.complexnatatie.controllers.handlers.responses.SubscriptionResponse;
import com.example.complexnatatie.entities.Subscription;
import com.example.complexnatatie.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record SubscriptionService(SubscriptionRepository subscriptionRepository) {

    public SubscriptionResponse findActiveByCustomerId(int customerId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findActiveByCustomerId(customerId);

        if (optionalSubscription.isEmpty()) {
            return new SubscriptionResponse();
        }

        return new SubscriptionResponse(SubscriptionBuilder.fromEntity(optionalSubscription.get()));
    }

}
