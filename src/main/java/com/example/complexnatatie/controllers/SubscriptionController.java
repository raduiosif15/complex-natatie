package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.SubscriptionDTO;
import com.example.complexnatatie.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('PORTER')")
    public ResponseEntity<List<SubscriptionDTO>> getAll() {
        return new ResponseEntity<>(subscriptionService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('PORTER')")
    public ResponseEntity<SubscriptionDTO> getById(@PathVariable int id) {
        return new ResponseEntity<>(subscriptionService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/customers/active/{customerId}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('PORTER')")
    public ResponseEntity<SubscriptionDTO> getByCustomerId(@PathVariable int customerId) {
        return new ResponseEntity<>(subscriptionService.findActiveByCustomerId(customerId), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<SubscriptionDTO> create(@RequestBody SubscriptionDTO subscriptionDTO) {
        return new ResponseEntity<>(subscriptionService.create(subscriptionDTO), HttpStatus.CREATED);
    }

}
