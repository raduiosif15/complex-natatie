package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.PaymentDTO;
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

    @GetMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentDTO>> getAll() {
        return new ResponseEntity<>(paymentService.getAll(), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> pay(@RequestBody PaymentDTO paymentDTO) {
        return new ResponseEntity<>(paymentService.pay(paymentDTO), HttpStatus.OK);
    }

}
