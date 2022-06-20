package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.SubscriptionBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.responses.SubscriptionResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.entities.Subscription;
import com.example.complexnatatie.repositories.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public record SubscriptionService(SubscriptionRepository subscriptionRepository,
                                  ContractService contractService) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public SubscriptionResponse findActiveByCustomerId(int customerId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findActiveByCustomerId(customerId);

        if (optionalSubscription.isEmpty()) {
            return new SubscriptionResponse();
        }

        return new SubscriptionResponse(SubscriptionBuilder.fromEntity(optionalSubscription.get()));
    }

    public List<SubscriptionDTO> getSubscriptionsByContractId(int contractId) {
        return SubscriptionBuilder.fromEntities(subscriptionRepository.findSubscriptionsByContractId(contractId));
    }

    public SubscriptionDTO getLastActiveSubscription(int contractId) {

        final List<SubscriptionDTO> subscriptions = getSubscriptionsByContractId(contractId);

        if (subscriptions.isEmpty()) {
            return null;
        }

        return subscriptions.get(subscriptions.size() - 1);

    }

    public int getContractMonthsLeftUnpaid(int contractId) {

        final ContractDTO contractDTO = contractService.getById(contractId);

        final SubscriptionDTO lastActiveSubscription = getLastActiveSubscription(contractId);

        final LocalDate contractEndDate = new java.sql.Date(contractDTO.getEndDate().getTime()).toLocalDate();
        final LocalDate subscriptionEndDate = new java.sql.Date(
                lastActiveSubscription == null
                        ? (new Date()).getTime()
                        : lastActiveSubscription.getEndDate().getTime()
        ).toLocalDate();

        return (int) ChronoUnit.MONTHS.between(
                subscriptionEndDate.withDayOfMonth(1),
                contractEndDate.withDayOfMonth(1));

    }

    public SubscriptionDTO createOrExtendSubscription(int contractId, int months) {

        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        final SubscriptionDTO lastActiveSubscription = getLastActiveSubscription(contractId);

        // if there is an active subscription, we extend that one with x months
        if (lastActiveSubscription != null) {

            if (date.before(lastActiveSubscription.getStartDate())) {

                LOGGER.error("Subscription start date before present");
                throw new CustomException("Subscription start date before present", HttpStatus.NOT_ACCEPTABLE);

            }

            if (date.before(lastActiveSubscription.getEndDate())) {

                calendar.setTime(lastActiveSubscription.getEndDate());
                calendar.add(Calendar.MONTH, months);

                lastActiveSubscription.setEndDate(calendar.getTime());
                Subscription subscription = SubscriptionBuilder.fromDTO(lastActiveSubscription);
                subscription = subscriptionRepository.save(subscription);

                return SubscriptionBuilder.fromEntity(subscription);
            }

        }

        // else create new subscription from scratch

        Subscription subscription = new Subscription();
        subscription.setContractId(contractId);
        subscription.setStartDate(date);
        calendar.setTime(date);

        // add x months -1 day to subscription
        calendar.add(Calendar.MONTH, months);
        calendar.add(Calendar.DATE, -1);
        subscription.setEndDate(calendar.getTime());

        subscription = subscriptionRepository.save(subscription);

        return SubscriptionBuilder.fromEntity(subscription);

    }

}
