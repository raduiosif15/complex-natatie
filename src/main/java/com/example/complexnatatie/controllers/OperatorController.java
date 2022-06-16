package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.services.OperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/operators")
public class OperatorController {

    private final OperatorService operatorService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OperatorDTO>> getAll() {
        return new ResponseEntity<>(operatorService.getAll(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OperatorDTO> update(@PathVariable int id, @RequestBody OperatorDTO operatorDTO) {
        return new ResponseEntity<>(operatorService.update(id, operatorDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OperatorDTO> delete(@PathVariable int id) {
        return new ResponseEntity<>(operatorService.delete(id), HttpStatus.OK);
    }

}
