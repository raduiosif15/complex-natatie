package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record PaymentService(PaymentRepository paymentRepository) {

    public List<PaymentDTO> getAll() {
        return PaymentBuilder.fromEntities(paymentRepository.findAll());
    }

}
