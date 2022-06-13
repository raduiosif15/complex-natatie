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

    @Column(name = "pay_date", nullable = false)
    private Date payDate;

    @Column(name = "pay_value", nullable = false)
    private double payValue;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "pay_type", nullable = false)
    private String payType;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

}
