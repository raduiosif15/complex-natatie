package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.ContractBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ContractException;
import com.example.complexnatatie.controllers.handlers.responses.ContractValidityResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.entities.Contract;
import com.example.complexnatatie.repositories.ContractRepository;
import com.example.complexnatatie.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public record ContractService(ContractRepository contractRepository, CustomerRepository customerRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractService.class);

    public List<ContractDTO> getAll() {
        return ContractBuilder.fromEntities(contractRepository.findAll());
    }

    public ContractValidityResponse checkValidContractExists(int customerId) {
        final Optional<Contract> optionalContract = contractRepository.getActiveContractByCustomerId(customerId);

        if (optionalContract.isPresent()) {
            final Contract contract = optionalContract.get();

            return new ContractValidityResponse(true, contract.getEndDate());
        }

        return new ContractValidityResponse(false, null);
    }

    public ContractDTO create(ContractDTO contractDTO) {
        final int customerId = contractDTO.getCustomerId();
        final ContractValidityResponse checkValidity = checkValidContractExists(customerId);

        if (checkValidity.isValid()) {
            LOGGER.error("Customer with id: {} already have an active contract until {}.", customerId, checkValidity.getDate());
            throw new ContractException("Customer with id: " + customerId + " already have an active contract until " + checkValidity.getDate(), HttpStatus.CONFLICT);
        }

        Contract contract = ContractBuilder.fromDTO(contractDTO);
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        contract.setStartDate(calendar.getTime());

        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);
        contract.setEndDate(calendar.getTime());

        contract = contractRepository.save(contract);
        return ContractBuilder.fromEntity(contract);
    }

}
