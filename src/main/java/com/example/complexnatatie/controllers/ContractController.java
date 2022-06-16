package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.services.ContractService;
import com.example.complexnatatie.controllers.handlers.responses.ContractAvailabilityResponse;
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

    @GetMapping(value = "/availability/{customerId}")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<ContractAvailabilityResponse> checkIfOtherContractExists(@PathVariable int customerId) {
        return new ResponseEntity<>(contractService.checkIfOtherContractExists(customerId), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<ContractDTO> create(@RequestBody ContractDTO contractDTO) {
        return new ResponseEntity<>(contractService.create(contractDTO), HttpStatus.CREATED);
    }
}
