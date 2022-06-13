package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.OperatorBuilder;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.repositories.OperatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record OperatorService(OperatorRepository operatorRepository) {

    public List<OperatorDTO> getAll() {
        return OperatorBuilder.fromEntities(operatorRepository.findAll());
    }

}
