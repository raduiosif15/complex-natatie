package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.services.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(value = "/create")
    public ResponseEntity<ContractDTO> create(@RequestBody ContractDTO contractDTO) {
        return new ResponseEntity<>(contractService.create(contractDTO), HttpStatus.OK);
    }

}
