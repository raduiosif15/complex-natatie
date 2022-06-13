package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    @Query(value = "SELECT subscription " +
            "FROM Subscription subscription " +
            "JOIN Customer customer ON customer.id = subscription.customerId " +
            "WHERE subscription.customerId = :customerId " +
            "ORDER BY subscription.endDate DESC ")
    List<Subscription> getSubscriptionsByCustomerId(int customerId);

}
