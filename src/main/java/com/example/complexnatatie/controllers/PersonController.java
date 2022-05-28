package com.example.complexnatatie.controllers;

import com.example.complexnatatie.bll.dtos.PersonDTO;
import com.example.complexnatatie.bll.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

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

}
