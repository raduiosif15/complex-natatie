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
public class ContractValidityResponse extends CustomResponse {

    private boolean valid;

    private ContractDTO contract;

    public ContractValidityResponse(String message, boolean valid, ContractDTO contract) {
        super(message);
        this.valid = valid;
        this.contract = contract;
    }
}
