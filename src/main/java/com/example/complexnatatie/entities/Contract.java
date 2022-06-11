package com.example.complexnatatie.entities;

import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.PersonDTO;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Contract {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    // todo: add cashier id

    // todo: create a builder class instead
    public static Contract fromContractDTO(ContractDTO contractDTO) {
        return Contract.builder()
                .id(contractDTO.getId())
                .createdDate(contractDTO.getCreatedDate())
                .expirationDate(contractDTO.getExpirationDate())
                .clientId(contractDTO.getClientId())
                .build();
    }

}
