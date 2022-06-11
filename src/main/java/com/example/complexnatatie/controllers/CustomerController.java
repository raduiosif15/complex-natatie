package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<CustomerDTO>> getByName(@RequestParam String name) {
        return new ResponseEntity<>(customerService.getByName(name), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<CustomerDTO> add(@RequestBody CustomerDTO customerDTO) {
        System.out.println("customer dto: " + customerDTO);
        return new ResponseEntity<>(customerService.save(customerDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable int id, @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.update(id, customerDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CustomerDTO> delete(@PathVariable int id) {
        return new ResponseEntity<>(customerService.delete(id), HttpStatus.OK);
    }

}
