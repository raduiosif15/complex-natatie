package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.services.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/contracts")
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAll() {
        return new ResponseEntity<>(contractService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/check-availability/{clientId}")
    public ResponseEntity<Optional<Object>> checkIfOtherContractExists(@PathVariable UUID clientId) {
        return new ResponseEntity<>(contractService.checkIfOtherContractExists(clientId), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ContractDTO> create(@RequestBody ContractDTO contractDTO) {
        return new ResponseEntity<>(contractService.create(contractDTO), HttpStatus.CREATED);
    }
}
