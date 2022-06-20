package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    @Query(value = "SELECT subscription " +
            "FROM Subscription subscription " +
            "JOIN Contract contract ON contract.id = subscription.contractId " +
            "WHERE contract.customerId = :customerId " +
            "AND ((subscription.startDate <= CURRENT_DATE) AND (subscription.endDate >= CURRENT_DATE)) ")
    Optional<Subscription> findActiveByCustomerId(int customerId);

    @Query("SELECT contract.subscriptions " +
            "FROM Contract contract " +
            "WHERE contract.id = :contractId ")
    List<Subscription> findSubscriptionsByContractId(int contractId);

}
