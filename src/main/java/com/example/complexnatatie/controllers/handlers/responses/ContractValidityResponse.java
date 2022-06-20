package com.example.complexnatatie.controllers.handlers.responses;

import com.example.complexnatatie.dtos.ContractDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractValidityResponse {
    private ContractDTO contract;
}
