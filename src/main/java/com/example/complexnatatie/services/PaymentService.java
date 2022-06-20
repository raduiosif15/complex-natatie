package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.controllers.handlers.exceptions.ContractException;
import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.entities.Payment;
import com.example.complexnatatie.entities.PaymentCash;
import com.example.complexnatatie.entities.PaymentPos;
import com.example.complexnatatie.repositories.PaymentCashRepository;
import com.example.complexnatatie.repositories.PaymentPosRepository;
import com.example.complexnatatie.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public record PaymentService(PaymentRepository paymentRepository,
                             PaymentPosRepository paymentPosRepository,
                             PaymentCashRepository paymentCashRepository,
                             ContractService contractService) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    // todo: delete this
    public List<PaymentDTO> getAll() {

        final List<Payment> allPayments = new ArrayList<>();

        allPayments.addAll(paymentPosRepository.findAll());
        allPayments.addAll(paymentCashRepository.findAll());

        return PaymentBuilder.fromEntities(allPayments);
    }

    public PaymentDTO pay(PaymentRequest paymentRequest) {

        final int customerId = paymentRequest.getCustomerId();
        final ContractDTO contractDTO = contractService.checkValidContractExists(customerId).getContract();

        if (contractDTO == null) {

            LOGGER.error("Customer with id: {} haven't got any active contract.", customerId);
            throw new ContractException("Customer with id: " + customerId + " haven't got any active contract.", HttpStatus.NOT_FOUND);

        }

        final Date date = new Date();
        final double value = paymentRequest.getMonths() * contractDTO.getMonthly();

        // todo: deny payment for more than 12 months or subscription over contractual interval
        // todo: create subscription

        if (paymentRequest.getType().getName().equals(PaymentType.POS.getName())) {

            PaymentPos paymentPos = new PaymentPos(
                    0,
                    date,
                    value,
                    paymentRequest.getDescription(),
                    paymentRequest.getType().getName(),
                    paymentRequest.getCustomerId()
            );

            paymentPos = paymentRepository.save(paymentPos);
            return PaymentBuilder.fromEntity(paymentPos);

        }

        PaymentCash paymentCash = new PaymentCash(
                0,
                date,
                value,
                paymentRequest.getDescription(),
                paymentRequest.getType().getName(),
                paymentRequest.getCustomerId()
        );

        paymentCash = paymentRepository.save(paymentCash);
        return PaymentBuilder.fromEntity(paymentCash);

    }
}
