package com.example.complexnatatie.services.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractAvailabilityResponse extends CustomResponse {

    private boolean available;

    private Date date;

    public ContractAvailabilityResponse(String message, boolean available, Date date) {
        super(message);
        this.available = available;
        this.date = date;
    }
}
