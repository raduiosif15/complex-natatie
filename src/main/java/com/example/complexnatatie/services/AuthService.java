package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.OperatorBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.CreateAccountException;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.entities.Operator;
import com.example.complexnatatie.repositories.OperatorRepository;
import com.example.complexnatatie.security.jwt.JwtUtils;
import com.example.complexnatatie.security.payload.requests.CreateAccountRequest;
import com.example.complexnatatie.security.payload.responses.JwtResponse;
import com.example.complexnatatie.security.payload.requests.LoginRequest;
import com.example.complexnatatie.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public record AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          OperatorRepository operatorRepository, PasswordEncoder encoder) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public JwtResponse auth(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUtcnId(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .token(jwt)
                .id(userDetails.getId())
                .utcnId(userDetails.getUtcnId())
                .roles(roles)
                .build();

    }


    public void createAccount(CreateAccountRequest createAccountRequest) {

        final String utcnId = createAccountRequest.getUtcnId();
        Optional<Operator> operatorOptional = operatorRepository.findByUtcnId(utcnId);

        if (operatorOptional.isPresent()) {
            LOGGER.error("Operator with utcnId: {} already exists", utcnId);
            throw new CreateAccountException("Operator with utcnId: " + utcnId + " already exists");
        }





        Operator operator = Operator.builder()
                .utcnId(utcnId)
                .password(encoder.encode(createAccountRequest.getPassword()))
                .build();
    }

}
