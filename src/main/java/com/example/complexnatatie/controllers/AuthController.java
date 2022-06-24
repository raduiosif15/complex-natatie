package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.CustomerCreateDTO;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.security.payload.requests.LoginRequest;
import com.example.complexnatatie.security.payload.responses.JwtResponse;
import com.example.complexnatatie.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/operator")
    public ResponseEntity<JwtResponse> authenticateOperator(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.authOperator(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    // todo: uncomment this
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> createOperator(@RequestBody OperatorDTO operatorDTO) {
        return new ResponseEntity<>(authService.createOperator(operatorDTO), HttpStatus.OK);
    }

    @PostMapping("/customer")
    public ResponseEntity<JwtResponse> authenticateCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.authCustomer(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/registerCustomer")
    // todo: uncomment this
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerCreateDTO customerDTO) {
        return new ResponseEntity<>(authService.createCustomer(customerDTO), HttpStatus.OK);
    }

}
