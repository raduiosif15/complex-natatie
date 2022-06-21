package com.example.complexnatatie.entities;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "monthly", nullable = false)
    private double monthly;

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "customer_type", nullable = false)
    private String customerType;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

}
