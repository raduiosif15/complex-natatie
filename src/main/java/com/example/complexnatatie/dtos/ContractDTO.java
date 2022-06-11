package com.example.complexnatatie.dtos;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ContractDTO {

    private int id;

    private int number;

    private Date startDate;

    private Date endDate;

    private int customerId;

}
