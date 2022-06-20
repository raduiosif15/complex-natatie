package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.dtos.SubscriptionDTO;
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
                             ContractService contractService,
                             SubscriptionService subscriptionService) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    // todo: delete this
    public List<PaymentDTO> getAll() {

        final List<Payment> allPayments = new ArrayList<>();

        allPayments.addAll(paymentPosRepository.findAll());
        allPayments.addAll(paymentCashRepository.findAll());

        return PaymentBuilder.fromEntities(allPayments);
    }

    public PaymentResponse pay(PaymentRequest paymentRequest) {

        final PaymentResponse paymentResponse = new PaymentResponse();

        final int customerId = paymentRequest.getCustomerId();
        final ContractDTO contractDTO = contractService.checkValidContractExists(customerId).getContract();

        if (contractDTO == null) {

            LOGGER.error("Customer with id: {} haven't got any active contract.", customerId);
            throw new CustomException("Customer with id: " + customerId + " haven't got any active contract.", HttpStatus.NOT_FOUND);

        }

        // deny payment for more than 12 months or subscription over contractual interval
        final int monthsToPay = paymentRequest.getMonths();

        if (monthsToPay > 12 || monthsToPay > subscriptionService.getContractMonthsLeftUnpaid(contractDTO.getId())) {

            LOGGER.error("Payment exceeds the contractual period");
            throw new CustomException("Payment exceeds the contractual period", HttpStatus.NOT_ACCEPTABLE);

        }


        final Date date = new Date();
        final double value = monthsToPay * contractDTO.getMonthly();

        // create/extend subscription
        final SubscriptionDTO subscriptionDTO = subscriptionService.createOrExtendSubscription(contractDTO.getId(), monthsToPay);

        paymentResponse.setSubscription(subscriptionDTO);

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
            paymentResponse.setPayment(PaymentBuilder.fromEntity(paymentPos));
            return paymentResponse;

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
        paymentResponse.setPayment(PaymentBuilder.fromEntity(paymentCash));
        return paymentResponse;

    }

}
