package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.SubscriptionBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.entities.Subscription;
import com.example.complexnatatie.repositories.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public record SubscriptionService(SubscriptionRepository subscriptionRepository) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<SubscriptionDTO> getAll() {
        return SubscriptionBuilder.fromEntities(subscriptionRepository.findAll());
    }

    public SubscriptionDTO getById(int id) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);

        if (optionalSubscription.isEmpty()) {
            LOGGER.error("Subscription with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Subscription with id: " + id + " was not found in database");
        }

        return SubscriptionBuilder.fromEntity(optionalSubscription.get());
    }

    public SubscriptionDTO getByCustomerId(int customerId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByCustomerId(customerId);

        if (optionalSubscription.isEmpty()) {
            LOGGER.error("Customer with id: {} haven't any active subscription", customerId);
            throw new ResourceNotFoundException("Customer with id: " + customerId + " haven't any active subscription");
        }

        return SubscriptionBuilder.fromEntity(optionalSubscription.get());
    }

    // todo: if we are creating three subscription in a row, they must be for three consecutive months
    public SubscriptionDTO create(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = SubscriptionBuilder.fromDTO(subscriptionDTO);
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        subscription.setStartDate(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        subscription.setEndDate(calendar.getTime());

        subscription = subscriptionRepository.save(subscription);
        return SubscriptionBuilder.fromEntity(subscription);
    }

}
