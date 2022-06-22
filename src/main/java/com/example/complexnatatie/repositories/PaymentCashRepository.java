package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Payment;
import com.example.complexnatatie.entities.PaymentCash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PaymentCashRepository extends JpaRepository<PaymentCash, Integer> {

    @Query(value = "SELECT payment " +
            "FROM PaymentCash payment " +
            "WHERE payment.date >= :startDate AND payment.date <= :endDate ")
    List<Payment> findByDate(Date startDate, Date endDate);

}
