package com.example.complexnatatie.bll.services;

import com.example.complexnatatie.bll.dtos.PersonDTO;
import com.example.complexnatatie.dao.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PersonService {
    private final PersonRepository personRepository;

    public List<PersonDTO> getAll() {
        return personRepository
                .findAll()
                .stream()
                .map(p -> PersonDTO.builder()
                        .id(p.getId())
                        .firstName(p.getFirstName())
                        .lastName(p.getLastName())
                        .email(p.getEmail())
                        .phone(p.getPhone())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
