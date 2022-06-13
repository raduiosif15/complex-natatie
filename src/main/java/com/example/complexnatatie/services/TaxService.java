package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.TaxBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.TaxDTO;
import com.example.complexnatatie.entities.Tax;
import com.example.complexnatatie.repositories.TaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record TaxService(TaxRepository taxRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaxService.class);

    public List<TaxDTO> getAll() {
        return TaxBuilder.fromEntities(taxRepository.findAll());
    }

    public TaxDTO save(TaxDTO taxDTO) {
        Tax tax = TaxBuilder.fromDTO(taxDTO);
        tax = taxRepository.save(tax);
        return TaxBuilder.fromEntity(tax);
    }

    public TaxDTO update(int id, TaxDTO taxDTO) {
        Optional<Tax> optionalTax = taxRepository.findById(id);

        if (optionalTax.isEmpty()) {
            LOGGER.error("Tax with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Tax with id: " + id + " was not found in database");
        }

        Tax tax = optionalTax.get();

        tax.setDescription(taxDTO.getDescription());
        tax.setTaxType(taxDTO.getTaxType().getName());
        tax.setTaxValue(taxDTO.getTaxValue());

        taxRepository.save(tax);

        return TaxBuilder.fromEntity(tax);
    }

    public TaxDTO delete(int id) {
        Optional<Tax> optionalTax = taxRepository.findById(id);

        if (optionalTax.isEmpty()) {
            LOGGER.error("Tax with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Tax with id: " + id + " was not found in database");
        }

        taxRepository.delete(optionalTax.get());

        return null;
    }
}
