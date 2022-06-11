package com.example.complexnatatie.services;

import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.PersonDTO;
import com.example.complexnatatie.entities.Person;
import com.example.complexnatatie.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public record PersonService(PersonRepository personRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    public List<PersonDTO> getAll() {
        return PersonDTO.fromPersons(personRepository.findAll());
    }

    public List<PersonDTO> getByName(String name) {
        final String newName = name.toLowerCase().replace("%20", " ");

        return PersonDTO.fromPersons(personRepository.getByName(newName));
    }

    public PersonDTO save(PersonDTO personDTO) {
        Person person = Person.fromPersonDTO(personDTO);
        person = personRepository.save(person);
        return PersonDTO.fromPerson(person);
    }

    public PersonDTO update(UUID id, PersonDTO personDTO) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isEmpty()) {
            LOGGER.error("Person with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Person with id: " + id + " was not found in database");
        }

        Person person = optionalPerson.get();

        final String phone = personDTO.getPhone();
        if (phone != null && !phone.isEmpty()) {
            person.setPhone(phone);
        }

        final String email = personDTO.getEmail();
        if (email != null && !email.isEmpty()) {
            person.setEmail(email);
        }

        personRepository.save(person);

        return PersonDTO.fromPerson(person);
    }

    public PersonDTO delete(UUID id) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isEmpty()) {
            LOGGER.error("Person with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Person with id: " + id + " was not found in database");
        }

        personRepository.delete(optionalPerson.get());

        return null;
    }
}
