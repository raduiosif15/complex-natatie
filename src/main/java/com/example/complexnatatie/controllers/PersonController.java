package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.PersonDTO;
import com.example.complexnatatie.entities.Person;
import com.example.complexnatatie.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAll() {
        return new ResponseEntity<>(personService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<PersonDTO>> getByName(@RequestParam String name) {
        return new ResponseEntity<>(personService.getByName(name), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Person> add(@RequestBody PersonDTO personDTO) {
        return new ResponseEntity<>(personService.save(personDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Person> update(@PathVariable UUID id, @RequestBody PersonDTO personDTO) {
        return new ResponseEntity<>(personService.update(id, personDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Person> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(personService.delete(id), HttpStatus.OK);
    }

}
