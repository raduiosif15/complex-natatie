package com.example.complexnatatie.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Email
    @Column(name = "utcn_id")
    private String utcnId;

    @Column(name = "password")
    private String password;

    @Column(name = "operator_type")
    private String operatorType;

}