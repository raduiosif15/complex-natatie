package com.example.complexnatatie.controllers.handlers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractValidityResponse extends CustomResponse {

    private boolean valid;

    private Date date;

    public ContractValidityResponse(String message, boolean valid, Date date) {
        super(message);
        this.valid = valid;
        this.date = date;
    }
}
