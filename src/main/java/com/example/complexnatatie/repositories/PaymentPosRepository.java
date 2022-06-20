package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Payment;
import com.example.complexnatatie.entities.PaymentPos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentPosRepository extends JpaRepository<PaymentPos, Integer> {


}
