package com.example.complexnatatie.controllers;

import com.example.complexnatatie.controllers.handlers.request.ReportRequest;
import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.dtos.PaymentWithCustomer;
import com.example.complexnatatie.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/preview/{customerId}/{months}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<Double> preview(@PathVariable int customerId, @PathVariable int months) {
        return new ResponseEntity<>(paymentService.preview(customerId, months), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentService.pay(paymentRequest), HttpStatus.CREATED);
    }

    @GetMapping("/daily")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentWithCustomer>> getDaily(@RequestBody ReportRequest reportRequest) {
        return new ResponseEntity<>(paymentService.getDaily(reportRequest), HttpStatus.OK);
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentWithCustomer>> getMonthly(@RequestBody ReportRequest reportRequest) {
        return new ResponseEntity<>(paymentService.getMonthly(reportRequest), HttpStatus.OK);
    }

}
