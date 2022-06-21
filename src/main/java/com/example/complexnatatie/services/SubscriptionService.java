package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.SubscriptionBuilder;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.controllers.handlers.responses.SubscriptionResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.entities.Subscription;
import com.example.complexnatatie.repositories.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public record SubscriptionService(SubscriptionRepository subscriptionRepository,
                                  ContractService contractService) {

    public SubscriptionResponse findActiveByCustomerId(int customerId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findActiveByCustomerId(customerId);

        if (optionalSubscription.isEmpty()) {
            return new SubscriptionResponse();
        }

        return new SubscriptionResponse(SubscriptionBuilder.fromEntity(optionalSubscription.get()));
    }

    public int getMonthsLeftUnpaid(int customerId) {

        final ContractDTO contractDTO = contractService.checkValidContractExists(customerId).getContract();

        final Optional<Subscription> optionalSubscription = subscriptionRepository.findActiveByCustomerId(customerId);

        Date date = new Date();
        if (optionalSubscription.isPresent()) {

            final Subscription subscription = optionalSubscription.get();

            date = subscription.getEndDate();

        }

        final LocalDate contractEndDate = new java.sql.Date(contractDTO.getEndDate().getTime()).toLocalDate();
        final LocalDate subscriptionEndDate = new java.sql.Date(date.getTime()).toLocalDate();

        return (int) ChronoUnit.MONTHS.between(
                subscriptionEndDate.withDayOfMonth(1),
                contractEndDate.withDayOfMonth(1));

    }

    public PaymentResponse createOrExtendSubscription(int customerId, int months) {

        PaymentResponse paymentResponse = new PaymentResponse();

        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        final Optional<Subscription> optionalSubscription = subscriptionRepository.findActiveByCustomerId(customerId);


        // if there is an active subscription, we extend that one with x months
        if (optionalSubscription.isPresent()) {

            Subscription subscription = optionalSubscription.get();

            calendar.setTime(subscription.getEndDate());
            calendar.add(Calendar.MONTH, months);

            subscription.setEndDate(calendar.getTime());
            subscription = subscriptionRepository.save(subscription);

            paymentResponse.setSubscription(SubscriptionBuilder.fromEntity(subscription));
            paymentResponse.setSubscriptionCreated(false);
            return paymentResponse;

        }

        // else create new subscription from scratch

        Subscription subscription = new Subscription();
        subscription.setCustomerId(customerId);
        subscription.setStartDate(date);
        calendar.setTime(date);

        // add x months -1 day to subscription
        calendar.add(Calendar.MONTH, months);
        calendar.add(Calendar.DATE, -1);
        subscription.setEndDate(calendar.getTime());

        subscription = subscriptionRepository.save(subscription);

        paymentResponse.setSubscription(SubscriptionBuilder.fromEntity(subscription));
        paymentResponse.setSubscriptionCreated(true);
        return paymentResponse;

    }

}
