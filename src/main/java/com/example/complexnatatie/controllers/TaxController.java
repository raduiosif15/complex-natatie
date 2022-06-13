package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.TaxDTO;
import com.example.complexnatatie.services.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/taxes")
public class TaxController {

    private final TaxService taxService;

    @GetMapping
    public ResponseEntity<List<TaxDTO>> getAll() {
        return new ResponseEntity<>(taxService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<TaxDTO> add(@RequestBody TaxDTO taxDTO) {
        return new ResponseEntity<>(taxService.save(taxDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TaxDTO> update(@PathVariable int id, @RequestBody TaxDTO taxDTO) {
        return new ResponseEntity<>(taxService.update(id, taxDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<TaxDTO> delete(@PathVariable int id) {
        return new ResponseEntity<>(taxService.delete(id), HttpStatus.OK);
    }

}
