package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.PaymentCash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PaymentCashRepository extends JpaRepository<PaymentCash, Integer> {

    @Query(value = "SELECT payment, customer, contract " +
            "FROM PaymentCash payment " +
            "JOIN Customer customer ON customer.id = payment.customerId " +
            "JOIN Contract contract ON contract.customerId = payment.customerId " +
            "WHERE payment.date >= :startDate AND payment.date <= :endDate ")
    List<Object[]> findByDate(Date startDate, Date endDate);

}
