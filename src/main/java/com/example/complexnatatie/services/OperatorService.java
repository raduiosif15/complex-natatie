package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.OperatorBuilder;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.entities.Operator;
import com.example.complexnatatie.repositories.OperatorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorService implements UserDetailsService {

    final OperatorRepository operatorRepository;

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
}
