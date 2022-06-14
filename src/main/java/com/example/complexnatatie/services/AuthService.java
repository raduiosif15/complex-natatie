package com.example.complexnatatie.services;

import com.example.complexnatatie.controllers.handlers.exceptions.CreateAccountException;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.entities.Operator;
import com.example.complexnatatie.repositories.OperatorRepository;
import com.example.complexnatatie.security.jwt.JwtUtils;
import com.example.complexnatatie.security.payload.requests.LoginRequest;
import com.example.complexnatatie.security.payload.responses.JwtResponse;
import com.example.complexnatatie.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          OperatorRepository operatorRepository, PasswordEncoder encoder) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public JwtResponse auth(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUtcnId(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Operator operator = operatorRepository.findByUtcnId(loginRequest.getUtcnId()).get();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        return JwtResponse.builder()
                .token(jwt)
                .id(operator.getId())
                .utcnId(operator.getUtcnId())
                .operatorType(operator.getOperatorType())
                .build();

    }

    public int createOperator(OperatorDTO operatorDTO) {

        final String utcnId = operatorDTO.getUtcnId();
        Optional<Operator> operatorOptional = operatorRepository.findByUtcnId(utcnId);

        if (operatorOptional.isPresent()) {
            LOGGER.error("Operator with utcnId: {} already exists", utcnId);
            throw new CreateAccountException("Operator with utcnId: " + utcnId + " already exists");
        }

        Operator operator = Operator.builder()
                .utcnId(operatorDTO.getUtcnId())
                .operatorType(operatorDTO.getOperatorType().getName())
                .password(encoder.encode(operatorDTO.getPassword()))
                .build();
        Operator newOperator = operatorRepository.save(operator);

        return newOperator.getId();
    }

}
