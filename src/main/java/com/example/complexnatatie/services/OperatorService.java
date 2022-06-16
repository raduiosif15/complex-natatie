package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.OperatorBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.entities.Operator;
import com.example.complexnatatie.repositories.OperatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperatorService implements UserDetailsService {

    final OperatorRepository operatorRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxService.class);

    public OperatorService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    public List<OperatorDTO> getAll() {
        return OperatorBuilder.fromEntities(operatorRepository.findAll());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Operator operator = operatorRepository.findByUtcnId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return OperatorBuilder.userDetailsBuilder(operator);

    }

    public OperatorDTO update(int id, OperatorDTO operatorDTO) {
        Optional<Operator> optionalOperator = operatorRepository.findById(id);

        if (optionalOperator.isEmpty()) {
            LOGGER.error("Operator with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Operator with id: " + id + " was not found in database");
        }

        Operator operator = optionalOperator.get();

        operator.setUtcnId(operatorDTO.getUtcnId());
        operator.setOperatorType(operatorDTO.getOperatorType().getName());

        operatorRepository.save(operator);

        return OperatorBuilder.fromEntity(operator);
    }

    public OperatorDTO delete(int id) {
        Optional<Operator> optionalOperator = operatorRepository.findById(id);

        if (optionalOperator.isEmpty()) {
            LOGGER.error("Operator with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Operator with id: " + id + " was not found in database");
        }

        operatorRepository.delete(optionalOperator.get());

        return null;
    }
}
