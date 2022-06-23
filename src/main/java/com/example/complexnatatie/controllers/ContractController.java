package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.ContractMonthlyStatistic;
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

    @GetMapping(value = "/{customerId}/valid")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<ContractValidityResponse> checkValidContractExists(@PathVariable int customerId) {
        return new ResponseEntity<>(contractService.checkValidContractExists(customerId), HttpStatus.OK);
    }

    @GetMapping(value = "/{customerId}/preview")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<ContractDTO> preview(@PathVariable int customerId) {
        return new ResponseEntity<>(contractService.create(customerId, true), HttpStatus.OK);
    }

    @PostMapping(value = "/{customerId}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<ContractDTO> create(@PathVariable int customerId) {
        return new ResponseEntity<>(contractService.create(customerId, false), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{customerId}/all")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<ContractDTO>> getAllByCustomerId(@PathVariable int customerId) {
        return new ResponseEntity<>(contractService.getAllByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping(value = "/statistic/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContractMonthlyStatistic>> getMonthStatisticForYear(@PathVariable int year) {
        return new ResponseEntity<>(contractService.getMonthStatisticForYear(year), HttpStatus.OK);
    }
}
