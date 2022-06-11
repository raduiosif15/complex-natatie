package com.example.complexnatatie.entities;

import com.example.complexnatatie.dtos.ContractDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    // todo: create a builder class instead
    public static Contract fromContractDTO(ContractDTO contractDTO) {
        return Contract.builder()
                .id(contractDTO.getId())
                .createdDate(contractDTO.getCreatedDate())
                .expirationDate(contractDTO.getExpirationDate())
                .customerId(contractDTO.getCustomerId())
                .build();
    }

}
