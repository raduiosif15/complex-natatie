package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.services.ContractService;
import com.example.complexnatatie.controllers.handlers.responses.ContractValidityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/contracts")
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<List<ContractDTO>> getAll() {
        return new ResponseEntity<>(contractService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{customerId}/valid")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<ContractValidityResponse> checkValidContractExists(@PathVariable int customerId) {
        return new ResponseEntity<>(contractService.checkValidContractExists(customerId), HttpStatus.OK);
    }

    @GetMapping(value = "/{customerId}/preview")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<ContractDTO> preview(@PathVariable int customerId) {
        return new ResponseEntity<>(contractService.create(customerId, true), HttpStatus.OK);
    }

    @PostMapping(value = "/{customerId}")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<ContractDTO> create(@PathVariable int customerId) {
        return new ResponseEntity<>(contractService.create(customerId, false), HttpStatus.CREATED);
    }
}
