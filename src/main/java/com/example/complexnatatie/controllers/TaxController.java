package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.TaxDTO;
import com.example.complexnatatie.services.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/taxes")
public class TaxController {

    private final TaxService taxService;

    @GetMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<TaxDTO>> getAll() {
        return new ResponseEntity<>(taxService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxDTO> add(@RequestBody TaxDTO taxDTO) {
        return new ResponseEntity<>(taxService.save(taxDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxDTO> update(@PathVariable int id, @RequestBody TaxDTO taxDTO) {
        return new ResponseEntity<>(taxService.update(id, taxDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        return new ResponseEntity<>(taxService.deleteById(id), HttpStatus.OK);
    }

}
