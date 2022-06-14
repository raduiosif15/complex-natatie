package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    @Query(value = "SELECT subscription " +
            "FROM Subscription subscription " +
            "JOIN Customer customer ON customer.id = subscription.customerId " +
            "WHERE subscription.customerId = :customerId " +
            "AND subscription.startDate <= CURRENT_DATE " +
            "AND subscription.endDate >= CURRENT_DATE ")
    Optional<Subscription> findByCustomerId(int customerId);

}
