package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.PaymentPos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PaymentPosRepository extends JpaRepository<PaymentPos, Integer> {

    @Query(value = "SELECT payment, customer, contract " +
            "FROM PaymentPos payment " +
            "JOIN Customer customer ON customer.id = payment.customerId " +
            "JOIN Contract contract ON contract.customerId = payment.customerId " +
            "WHERE payment.date >= :startDate AND payment.date <= :endDate ")
    List<Object[]> findByDate(Date startDate, Date endDate);

    @Query(nativeQuery = true,
            value = "SELECT SUM(payment_pos.value), customer.type, to_char(date, 'Mon') AS mon " +
                    "FROM payment_pos " +
                    "JOIN customer ON payment_pos.customer_id = customer.id " +
                    "WHERE extract(year FROM payment_pos.date) = 2022 " +
                    "GROUP BY customer.type, to_char(date, 'Mon') ")
    List<Object[]> getMonthStatisticForYear(int year);

}
