package com.example.complexnatatie.controllers;

import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.services.OperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/operators")
public class OperatorController {

    private final OperatorService operatorService;

    @GetMapping
    public ResponseEntity<List<OperatorDTO>> getAll() {
        return new ResponseEntity<>(operatorService.getAll(), HttpStatus.OK);
    }

}
