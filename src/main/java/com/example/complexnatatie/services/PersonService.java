package com.example.complexnatatie.services;

import com.example.complexnatatie.dtos.PersonDTO;
import com.example.complexnatatie.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PersonService {
    private final PersonRepository personRepository;

    public List<PersonDTO> getAll() {
        return PersonDTO.fromPersons(personRepository.findAll());
    }

    public List<PersonDTO> getByName(String name) {
        final String newName = name.toLowerCase().replace("%20", " ");

        return PersonDTO.fromPersons(personRepository.getByName(newName));
    }
}
