package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.entities.PaymentCash;
import com.example.complexnatatie.entities.PaymentPos;
import com.example.complexnatatie.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record PaymentService(PaymentRepository paymentRepository) {

    public List<PaymentDTO> getAll() {
        return PaymentBuilder.fromEntities(paymentRepository.findAll());
    }

    public PaymentDTO pay(PaymentDTO paymentDTO) {

        if (PaymentBuilder.fromDTO(paymentDTO) instanceof PaymentPos paymentPOS) {

            System.out.println("cu cardu");

            paymentPOS = paymentRepository.save(paymentPOS);
            return PaymentBuilder.fromEntity(paymentPOS);
        }

        System.out.println("cashhhh");


        PaymentCash paymentCash = (PaymentCash) PaymentBuilder.fromDTO(paymentDTO);
        paymentCash = paymentRepository.save(paymentCash);
        return PaymentBuilder.fromEntity(paymentCash);

    }
}
