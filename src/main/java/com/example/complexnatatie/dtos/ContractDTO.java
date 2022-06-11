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

    private Date createdDate;

    private Date expirationDate;

    private int customerId;

}
