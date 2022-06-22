package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.CustomerBuilder;
import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.request.ReportRequest;
import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.dtos.PaymentWithCustomer;
import com.example.complexnatatie.entities.Customer;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public record PaymentService(PaymentRepository paymentRepository,
                             PaymentPosRepository paymentPosRepository,
                             PaymentCashRepository paymentCashRepository,
                             ContractService contractService,
                             SubscriptionService subscriptionService) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public double preview(int customerId, int months) {

        final ContractDTO contractDTO = contractService.checkValidContractExists(customerId).getContract();

        if (contractDTO == null) {

            LOGGER.error("Customer with id: {} haven't got any active contract.", customerId);
            throw new CustomException("Customer with id: " + customerId + " haven't got any active contract.", HttpStatus.NOT_FOUND);

        }

        if (months > 12 || months > subscriptionService.getMonthsLeftUnpaid(customerId)) {

            LOGGER.error("Payment exceeds the contractual period");
            throw new CustomException("Payment exceeds the contractual period", HttpStatus.NOT_ACCEPTABLE);

        }

        return months * contractDTO.getMonthly();

    }


    public PaymentResponse pay(PaymentRequest paymentRequest) {

        final int customerId = paymentRequest.getCustomerId();
        final ContractDTO contractDTO = contractService.checkValidContractExists(customerId).getContract();

        if (contractDTO == null) {

            LOGGER.error("Customer with id: {} haven't got any active contract.", customerId);
            throw new CustomException("Customer with id: " + customerId + " haven't got any active contract.", HttpStatus.NOT_FOUND);

        }

        // deny payment for more than 12 months or subscription over contractual interval
        final int monthsToPay = paymentRequest.getMonths();

        if (monthsToPay > 12 || monthsToPay > subscriptionService.getMonthsLeftUnpaid(customerId)) {

            LOGGER.error("Payment exceeds the contractual period");
            throw new CustomException("Payment exceeds the contractual period", HttpStatus.NOT_ACCEPTABLE);

        }


        final Date date = new Date();
        final double value = monthsToPay * contractDTO.getMonthly();

        // create/extend subscription
        final PaymentResponse paymentResponse = subscriptionService.createOrExtendSubscription(customerId, monthsToPay);

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

    public List<PaymentDTO> getDaily(ReportRequest reportRequest) {

        final List<Payment> allPayments = new ArrayList<>();

        final Date dateFromRequest = reportRequest.getDate();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFromRequest);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        final Date startDate = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.SECOND, -1);
        final Date endDate = calendar.getTime();

        final List<Object[]> objects = paymentCashRepository.findByDate2(startDate, endDate);

        final List<PaymentWithCustomer> paymentWithCustomerList = new ArrayList<>();

        for (Object[] objDetails: objects) {

            final PaymentWithCustomer paymentWithCustomer = new PaymentWithCustomer();

            paymentWithCustomer.setPayment(PaymentBuilder.fromEntity((Payment) (objDetails[0])));
            paymentWithCustomer.setCustomer(CustomerBuilder.fromEntity((Customer) (objDetails[1])));


        }

        System.out.println("paymentWithCashList: " + paymentWithCustomerList.toString());

//
//        allPayments.addAll(paymentCashRepository.findByDate(startDate, endDate));
//        allPayments.addAll(paymentPosRepository.findByDate(startDate, endDate));

        return PaymentBuilder.fromEntities(allPayments);

    }

    public List<PaymentDTO> getMonthly(ReportRequest reportRequest) {

        final List<Payment> allPayments = new ArrayList<>();

        final Date dateFromRequest = reportRequest.getDate();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFromRequest);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        final Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        final Date endDate = calendar.getTime();

        allPayments.addAll(paymentCashRepository.findByDate(startDate, endDate));
        allPayments.addAll(paymentPosRepository.findByDate(startDate, endDate));

        return PaymentBuilder.fromEntities(allPayments);

    }
}
