package com.example.complexnatatie.dtos;

import com.example.complexnatatie.entities.Contract;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ContractDTO {

    private int id;

    private Date createdDate;

    private Date expirationDate;

    private int customerId;

    public static ContractDTO fromContract(Contract contract) {
        return ContractDTO.builder()
                .id(contract.getId())
                .createdDate(contract.getCreatedDate())
                .expirationDate(contract.getExpirationDate())
                .customerId(contract.getCustomerId())
                .build();
    }

    public static List<ContractDTO> fromContracts(List<Contract> contracts) {
        return contracts.stream().map(ContractDTO::fromContract).collect(Collectors.toList());
    }


}
