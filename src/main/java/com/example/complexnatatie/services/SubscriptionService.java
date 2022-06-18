package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.SubscriptionBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.entities.Subscription;
import com.example.complexnatatie.repositories.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record SubscriptionService(SubscriptionRepository subscriptionRepository) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public SubscriptionDTO findActiveByCustomerId(int customerId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findActiveByCustomerId(customerId);

        if (optionalSubscription.isEmpty()) {
            LOGGER.error("Customer with id: {} haven't any active subscription", customerId);
            throw new ResourceNotFoundException("Customer with id: " + customerId + " haven't any active subscription");
        }

        return SubscriptionBuilder.fromEntity(optionalSubscription.get());
    }

}
