package com.example.complexnatatie.services;

import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.PersonDTO;
import com.example.complexnatatie.entities.Contract;
import com.example.complexnatatie.repositories.ContractRepository;
import com.example.complexnatatie.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public record ContractService(ContractRepository contractRepository, PersonRepository personRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractService.class);

    public List<ContractDTO> getAll() {
        return ContractDTO.fromContracts(contractRepository.findAll());
    }

    // todo:
    // search client's active contract (if any)
    // if true : return error message for creating a new one
    // else: calculate total, display it and after confirmation create contract

    // calculate total by contract role/rank

    public Optional<Date> checkIfOtherContractExists(UUID clientId) {
        final List<Contract> contracts = contractRepository.getContractsByClientId(clientId);

        if (contracts.isEmpty()) {
            return Optional.empty();
        }

        final Date firstContractExpirationDate = contracts.get(0).getExpirationDate();
        final Date date = new Date();


        if (firstContractExpirationDate.before(date)) {
            return Optional.of(firstContractExpirationDate);
        }

        return Optional.empty();
    }

    public ContractDTO create(ContractDTO contractDTO) {
        // todo:
        final UUID clientId = contractDTO.getClientId();

        Contract contract = Contract.fromContractDTO(contractDTO);

        // set date
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        contract.setCreatedDate(calendar.getTime());

        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);
        contract.setExpirationDate(calendar.getTime());

        contract = contractRepository.save(contract);
        return ContractDTO.fromContract(contract);
    }

}
