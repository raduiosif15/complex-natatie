package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.SubscriptionBuilder;
import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.entities.Subscription;
import com.example.complexnatatie.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public record SubscriptionService(SubscriptionRepository subscriptionRepository) {

    public List<SubscriptionDTO> getAll() {
        return SubscriptionBuilder.fromEntities(subscriptionRepository.findAll());
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
