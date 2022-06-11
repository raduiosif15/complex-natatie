package com.example.complexnatatie.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "tax_type", nullable = false)
    private CustomerType taxType;

    @Column(name = "tax_value", nullable = false)
    private int taxValue;

}