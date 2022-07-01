package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.CustomerCreateDTO;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.security.payload.requests.LoginRequest;
import com.example.complexnatatie.security.payload.responses.JwtCustomerResponse;
import com.example.complexnatatie.security.payload.responses.JwtOperatorResponse;
import com.example.complexnatatie.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/operator")
    public ResponseEntity<JwtOperatorResponse> authenticateOperator(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.authOperator(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> createOperator(@Valid  @RequestBody OperatorDTO operatorDTO) {
        return new ResponseEntity<>(authService.createOperator(operatorDTO), HttpStatus.CREATED);
    }

    @PostMapping("/customer")
    public ResponseEntity<JwtCustomerResponse> authenticateCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.authCustomer(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/registerCustomer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO customerDTO) {
        return new ResponseEntity<>(authService.createCustomer(customerDTO), HttpStatus.CREATED);
    }

}
