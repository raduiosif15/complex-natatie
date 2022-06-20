package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.ContractBuilder;
import com.example.complexnatatie.builders.CustomerBuilder;
import com.example.complexnatatie.builders.TaxBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ContractException;
import com.example.complexnatatie.controllers.handlers.responses.ContractValidityResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.dtos.TaxDTO;
import com.example.complexnatatie.entities.Contract;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.entities.Tax;
import com.example.complexnatatie.repositories.ContractRepository;
import com.example.complexnatatie.repositories.CustomerRepository;
import com.example.complexnatatie.repositories.TaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public record ContractService(ContractRepository contractRepository, CustomerRepository customerRepository,
                              TaxRepository taxRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractService.class);

    public List<ContractDTO> getAll() {
        return ContractBuilder.fromEntities(contractRepository.findAll());
    }

    public ContractValidityResponse checkValidContractExists(int customerId) {
        final Optional<Contract> optionalContract = contractRepository.getActiveContractByCustomerId(customerId);

        if (optionalContract.isPresent()) {
            final Contract contract = optionalContract.get();

            return new ContractValidityResponse(true, ContractBuilder.fromEntity(contract));
        }

        return new ContractValidityResponse(false, null);
    }

    public ContractDTO create(int customerId, boolean isPreview) {
        final Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty()) {
            LOGGER.error("Customer with id: {} doesn't exist.", customerId);
            throw new ResourceAccessException("Customer with id: " + customerId + " doesn't exist.");
        }

        final CustomerDTO customerDTO = CustomerBuilder.fromEntity(optionalCustomer.get());

        final ContractValidityResponse checkValidity = checkValidContractExists(customerId);
        if (checkValidity.isValid()) {
            LOGGER.error("Customer with id: {} already have an active contract until {}.", customerId, checkValidity.getContract().getEndDate());
            throw new ContractException("Customer with id: " + customerId + " already have an active contract until " + checkValidity.getContract().getEndDate(), HttpStatus.CONFLICT);
        }

        final ContractDTO contractDTO = new ContractDTO();
        contractDTO.setCustomerId(customerId);
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        contractDTO.setStartDate(calendar.getTime());

        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);
        contractDTO.setEndDate(calendar.getTime());

        final String type = customerDTO.getType().getName();

        contractDTO.setCustomerType(customerDTO.getType());

        final Optional<Tax> optionalTax = taxRepository.findByType(type);

        if (optionalTax.isEmpty()) {
            LOGGER.error("Tax with type: {} doesn't exist.", type);
            throw new ResourceAccessException("Tax with type: " + type + " doesn't exist.");
        }

        final TaxDTO taxDTO = TaxBuilder.fromEntity(optionalTax.get());

        final double total = taxDTO.getValue();
        final double monthly = total / 12;

        contractDTO.setTotal(total);
        contractDTO.setMonthly(monthly);

        if (!isPreview) {
            Contract contract = ContractBuilder.fromDTO(contractDTO);
            contract = contractRepository.save(contract);
            return ContractBuilder.fromEntity(contract);
        }

        return contractDTO;
    }

}
