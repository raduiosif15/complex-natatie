package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.entities.Payment;
import com.example.complexnatatie.entities.PaymentCash;
import com.example.complexnatatie.entities.PaymentPos;
import com.example.complexnatatie.repositories.PaymentCashRepository;
import com.example.complexnatatie.repositories.PaymentPosRepository;
import com.example.complexnatatie.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public record PaymentService(PaymentRepository paymentRepository,
                             PaymentPosRepository paymentPosRepository,
                             PaymentCashRepository paymentCashRepository) {

    // todo: delete this
    public List<PaymentDTO> getAll() {

        final List<Payment> allPayments = new ArrayList<>();

        allPayments.addAll(paymentPosRepository.findAll());
        allPayments.addAll(paymentCashRepository.findAll());

        return PaymentBuilder.fromEntities(allPayments);
    }

    public PaymentDTO pay(PaymentDTO paymentDTO) {

        if (PaymentBuilder.fromDTO(paymentDTO) instanceof PaymentPos paymentPOS) {
            paymentPOS = paymentRepository.save(paymentPOS);
            return PaymentBuilder.fromEntity(paymentPOS);
        }

        PaymentCash paymentCash = (PaymentCash) PaymentBuilder.fromDTO(paymentDTO);
        paymentCash = paymentRepository.save(paymentCash);
        return PaymentBuilder.fromEntity(paymentCash);

    }
}
