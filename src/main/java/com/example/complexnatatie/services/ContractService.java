package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.ContractBuilder;
import com.example.complexnatatie.builders.CustomerBuilder;
import com.example.complexnatatie.builders.TaxBuilder;
import com.example.complexnatatie.builders.helpers.CustomerType;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.responses.ContractValidityResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.ContractMonthlyStatistic;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.dtos.TaxDTO;
import com.example.complexnatatie.entities.Contract;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.entities.Tax;
import com.example.complexnatatie.repositories.ContractRepository;
import com.example.complexnatatie.repositories.CustomerRepository;
import com.example.complexnatatie.repositories.TaxRepository;
import com.example.complexnatatie.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigInteger;
import java.util.*;

@Service
public record ContractService(ContractRepository contractRepository, CustomerRepository customerRepository,
                              TaxRepository taxRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractService.class);

    public ContractValidityResponse checkValidContractExists(int customerId) {
        final Optional<Contract> optionalContract = contractRepository.getActiveContractByCustomerId(customerId);

        if (optionalContract.isPresent()) {
            final Contract contract = optionalContract.get();

            return new ContractValidityResponse(ContractBuilder.fromEntity(contract));
        }

        return new ContractValidityResponse();
    }

    public ContractValidityResponse checkSelfValid(Authentication authentication) {

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return checkValidContractExists(userDetails.getId());

    }

    public ContractDTO create(int customerId, boolean isPreview) {
        final Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty()) {
            LOGGER.error("Customer with id: {} doesn't exist.", customerId);
            throw new ResourceAccessException("Customer with id: " + customerId + " doesn't exist.");
        }

        final CustomerDTO customerDTO = CustomerBuilder.fromEntity(optionalCustomer.get());

        final ContractValidityResponse checkValidity = checkValidContractExists(customerId);
        if (checkValidity.getContract() != null) {
            LOGGER.error("Customer with id: {} already have an active contract until {}.", customerId, checkValidity.getContract().getEndDate());
            throw new CustomException("Customer with id: " + customerId + " already have an active contract until " + checkValidity.getContract().getEndDate(), HttpStatus.CONFLICT);
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

    public List<ContractDTO> getAllByCustomerId(int customerId) {
        final List<Contract> contractList = contractRepository.getAllByCustomerId(customerId);

        return ContractBuilder.fromEntities(contractList);
    }

    public List<ContractDTO> getAllSelf(Authentication authentication) {

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return getAllByCustomerId(userDetails.getId());

    }

    public List<ContractMonthlyStatistic> getMonthStatisticForYear(int year) {
        final List<Object[]> statistics = contractRepository.getMonthStatisticForYear(year);

        final List<ContractMonthlyStatistic> contractMonthlyStatistics = new ArrayList<>();

        for (Object[] statistic : statistics) {

            final ContractMonthlyStatistic contractMonthlyStatistic = new ContractMonthlyStatistic();

            contractMonthlyStatistic.setCount((BigInteger) statistic[0]);
            contractMonthlyStatistic.setType(new CustomerType((String) statistic[1]));
            contractMonthlyStatistic.setMonth((String) statistic[2]);

            contractMonthlyStatistics.add(contractMonthlyStatistic);

        }

        return contractMonthlyStatistics;
    }

}
