package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.ContractBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ContractException;
import com.example.complexnatatie.controllers.handlers.responses.ContractAvailabilityResponse;
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

@Service
public record ContractService(ContractRepository contractRepository, CustomerRepository customerRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractService.class);

    public List<ContractDTO> getAll() {
        return ContractBuilder.fromEntities(contractRepository.findAll());
    }

    // todo:
    // search customer's active contract (if any)
    // if true : return error message for creating a new one
    // else: calculate total, display it and after confirmation create contract

    // calculate total by contract customerType

    public ContractAvailabilityResponse checkIfOtherContractExists(int customerId) {
        final List<Contract> contracts = contractRepository.getContractsByCustomerId(customerId);

        if (contracts.isEmpty()) {
            return new ContractAvailabilityResponse(true, null);
        }

        final Date firstContractExpirationDate = contracts.get(0).getEndDate();
        final Date date = new Date();

        if (firstContractExpirationDate.after(date)) {
            return new ContractAvailabilityResponse(false, firstContractExpirationDate);
        }

        return new ContractAvailabilityResponse(true, null);
    }

    public ContractDTO create(ContractDTO contractDTO) {
        final int customerId = contractDTO.getCustomerId();
        final ContractAvailabilityResponse checkAvailability = checkIfOtherContractExists(customerId);

        if (!checkAvailability.isAvailable()) {
            LOGGER.error("Customer with id: {} already have an active contract.", customerId);
            throw new ContractException("Customer with id: " + customerId + " already have an active contract", HttpStatus.CONFLICT);
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
