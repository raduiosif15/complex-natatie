package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.PaymentOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PaymentOnlineRepository extends JpaRepository<PaymentOnline, Integer> {

    @Query(value = "SELECT payment, customer, contract " +
            "FROM PaymentOnline payment " +
            "JOIN Customer customer ON customer.id = payment.customerId " +
            "JOIN Contract contract ON contract.customerId = payment.customerId " +
            "WHERE (payment.date >= :startDate AND payment.date <= :endDate) " +
            "AND (contract.startDate <= payment.date AND payment.date <= contract.endDate) ")
    List<Object[]> findByDate(Date startDate, Date endDate);

    @Query(nativeQuery = true,
            value = "SELECT SUM(payment_online.value), customer.type, to_char(date, 'Mon') AS mon, extract(month from date) " +
                    "FROM payment_online " +
                    "JOIN customer ON payment_online.customer_id = customer.id " +
                    "WHERE extract(year FROM payment_online.date) = :year " +
                    "GROUP BY customer.type, to_char(date, 'Mon'), extract(month from date)" +
                    "ORDER BY extract(month from date) ")
    List<Object[]> getMonthStatisticForYear(int year);

}
