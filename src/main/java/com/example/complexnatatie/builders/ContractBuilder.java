package com.example.complexnatatie.builders;

import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.entities.Contract;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ContractBuilder {

    public static ContractDTO fromEntity(Contract contract) {
        return ContractDTO.builder()
                .id(contract.getId())
                .number(contract.getNumber())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .customerId(contract.getCustomerId())
                .build();
    }

    public static List<ContractDTO> fromEntities(List<Contract> contracts) {
        return contracts.stream().map(ContractBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Contract fromDTO(ContractDTO contractDTO) {
        return Contract.builder()
                .id(contractDTO.getId())
                .number(contractDTO.getNumber())
                .startDate(contractDTO.getStartDate())
                .endDate(contractDTO.getEndDate())
                .customerId(contractDTO.getCustomerId())
                .build();
    }
}
