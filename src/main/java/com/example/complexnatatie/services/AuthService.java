package com.example.complexnatatie.services;

import com.example.complexnatatie.controllers.handlers.exceptions.CreateAccountException;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.CustomerCreateDTO;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.entities.Operator;
import com.example.complexnatatie.repositories.CustomerRepository;
import com.example.complexnatatie.repositories.OperatorRepository;
import com.example.complexnatatie.security.jwt.JwtUtils;
import com.example.complexnatatie.security.payload.requests.LoginRequest;
import com.example.complexnatatie.security.payload.responses.JwtResponse;
import com.example.complexnatatie.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record AuthService(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          OperatorRepository operatorRepository,
                          PasswordEncoder encoder,
                          CustomerRepository customerRepository) {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public JwtResponse authOperator(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUtcnId(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Operator> optionalOperator = operatorRepository.findByUtcnId(loginRequest.getUtcnId());

        if (optionalOperator.isEmpty()) {
            throw new ResourceNotFoundException("Operator with utcnId: " + loginRequest.getUtcnId() + " doesn't exist.");
        }

        Operator operator = optionalOperator.get();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        return JwtResponse.builder()
                .token(jwt)
                .id(operator.getId())
                .utcnId(operator.getUtcnId())
                .type(operator.getType())
                .build();

    }

    public JwtResponse authCustomer(LoginRequest loginRequest) {

        System.out.println("authentication: " + loginRequest.toString());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUtcnId(), loginRequest.getPassword()));

        System.out.println("authentication: " + authentication.toString());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        Optional<Customer> optionalCustomer = customerRepository.getByUtcnId(loginRequest.getUtcnId());

        if (optionalCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Customer with utcnId: " + loginRequest.getUtcnId() + " doesn't exist.");
        }

        Customer customer = optionalCustomer.get();

        return JwtResponse.builder()
                .token(jwt)
                .id(customer.getId())
                .utcnId(customer.getUtcnID())
                .type(customer.getType())
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

    public int createCustomer(CustomerCreateDTO customerDTO) {

        if (customerDTO.getCodeID() > 0) {
            final Optional<Customer> optionalCustomer = customerRepository.getByCodeID(customerDTO.getCodeID());

            if (optionalCustomer.isPresent()) {
                LOGGER.error("Customer with code id: {} already exist in database", customerDTO.getCodeID());
                throw new CustomException(
                        "Clientul cu id-ul legitimatiei " + customerDTO.getCodeID() + " exista deja salvat in sistem",
                        HttpStatus.CONFLICT
                );
            }
        }

        if (customerDTO.getUtcnID() != null && !customerDTO.getUtcnID().isEmpty()) {
            final Optional<Customer> optionalCustomer = customerRepository.getByUtcnId(customerDTO.getUtcnID());

            if (optionalCustomer.isPresent()) {
                LOGGER.error("Customer with utcn id: {} already exist in database", customerDTO.getUtcnID());
                throw new CustomException(
                        "Clientul cu adresa de email " + customerDTO.getUtcnID() + " exista deja salvat in sistem",
                        HttpStatus.CONFLICT
                );
            }
        }

        Customer customer = Customer.builder()
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .phone(customerDTO.getPhone())
                .cnp(customerDTO.getCnp())
                .utcnID(customerDTO.getUtcnID())
                .codeID(customerDTO.getCodeID())
                .type(customerDTO.getType().getName())
                .password(encoder.encode(customerDTO.getPassword()))
                .build();
        Customer newCustomer = customerRepository.save(customer);

        return newCustomer.getId();
    }
}
