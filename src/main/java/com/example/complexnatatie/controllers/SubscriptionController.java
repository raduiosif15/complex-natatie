package com.example.complexnatatie.controllers;

import com.example.complexnatatie.controllers.handlers.responses.SubscriptionResponse;
import com.example.complexnatatie.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/subscriptions")
public class SubscriptionController {

    // subscriptions are created by the system once payment is confirmed
    private final SubscriptionService subscriptionService;

    @GetMapping(value = "/{customerId}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('PORTER') or hasRole('ADMIN')")
    public ResponseEntity<SubscriptionResponse> findActiveByCustomerId(@PathVariable int customerId) {
        return new ResponseEntity<>(subscriptionService.findActiveByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping(value = "/unpaid/{customerId}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<Integer> getMonthsLeftUnpaid(@PathVariable int customerId) {
        return new ResponseEntity<>(subscriptionService.getMonthsLeftUnpaid(customerId), HttpStatus.OK);
    }

}
