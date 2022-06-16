package com.example.complexnatatie.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "value", nullable = false)
    private double value;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

}
