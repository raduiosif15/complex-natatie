package com.example.complexnatatie.dtos;

import com.example.complexnatatie.entities.Contract;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ContractDTO {

    private UUID id;

    private Date createdDate;

    private Date expirationDate;

    private UUID clientId;

    public static ContractDTO fromContract(Contract contract) {
        return ContractDTO.builder()
                .id(contract.getId())
                .createdDate(contract.getCreatedDate())
                .expirationDate(contract.getExpirationDate())
                .clientId(contract.getClientId())
                .build();
    }

    public static List<ContractDTO> fromContracts(List<Contract> contracts) {
        return contracts.stream().map(ContractDTO::fromContract).collect(Collectors.toList());
    }


}
