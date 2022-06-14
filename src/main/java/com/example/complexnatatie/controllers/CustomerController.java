package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<List<CustomerDTO>> getByName(@RequestParam String name) {
        return new ResponseEntity<>(customerService.getByName(name), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<CustomerDTO> add(@RequestBody CustomerDTO customerDTO) {
        System.out.println("customer dto: " + customerDTO);
        return new ResponseEntity<>(customerService.save(customerDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<CustomerDTO> update(@PathVariable int id, @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.update(id, customerDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<CustomerDTO> delete(@PathVariable int id) {
        return new ResponseEntity<>(customerService.delete(id), HttpStatus.OK);
    }

}
