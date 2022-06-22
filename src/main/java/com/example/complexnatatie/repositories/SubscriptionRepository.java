package com.example.complexnatatie.repositories;

import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    @Query(value = "SELECT subscription " +
            "FROM Subscription subscription " +
            "WHERE subscription.customerId = :customerId " +
            "AND subscription.endDate >= CURRENT_DATE ")
    Optional<Subscription> findActiveByCustomerId(int customerId);

}
