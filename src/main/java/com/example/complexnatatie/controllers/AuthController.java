package com.example.complexnatatie.controllers;

import com.example.complexnatatie.security.payload.responses.JwtResponse;
import com.example.complexnatatie.security.payload.requests.LoginRequest;
import com.example.complexnatatie.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        System.out.println("Login request: " + loginRequest.toString());

        return new ResponseEntity<>(authService.auth(loginRequest), HttpStatus.OK);
    }

}
