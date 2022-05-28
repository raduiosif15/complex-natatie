package com.example.complexnatatie.bll.services;

import com.example.complexnatatie.bll.dtos.PersonDTO;
import com.example.complexnatatie.dao.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
