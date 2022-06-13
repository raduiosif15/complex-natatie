package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
