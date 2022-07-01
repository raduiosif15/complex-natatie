package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{nameOrCodeID}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('PORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<CustomerDTO>> getByNameOrCodeID(@PathVariable String nameOrCodeID) {
        return new ResponseEntity<>(customerService.getByNameOrCodeID(nameOrCodeID), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> add(@Valid @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.save(customerDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> update(@PathVariable int id, @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.update(id, customerDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        return new ResponseEntity<>(customerService.deleteById(id), HttpStatus.OK);
    }

}
