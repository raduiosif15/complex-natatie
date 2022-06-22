package com.example.complexnatatie.repositories;

import com.example.complexnatatie.dtos.PaymentWithCustomer;
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


    @Query(value = "SELECT payment.id as id, customer.firstName as firstName " +
            "FROM PaymentCash payment " +
            "JOIN Customer customer ON customer.id = payment.id " +
            "WHERE payment.date >= :startDate AND payment.date <= :endDate ")
    List<Object[]> findByDate2(Date startDate, Date endDate);

}
