package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.OperatorBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.CreateAccountException;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
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
                .type(operator.getType())
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
                .type(operatorDTO.getType().getName())
                .password(encoder.encode(operatorDTO.getPassword()))
                .build();
        Operator newOperator = operatorRepository.save(operator);

        return newOperator.getId();
    }


    public OperatorDTO changePassword(int id, String password) {
        Optional<Operator> optionalOperator = operatorRepository.findById(id);

        if (optionalOperator.isEmpty()) {
            LOGGER.error("Operator with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Operator with id: " + id + " was not found in database");
        }

        Operator operator = optionalOperator.get();

        operator.setPassword(encoder.encode(password));

        operatorRepository.save(operator);

        return OperatorBuilder.fromEntity(operator);
    }

}
