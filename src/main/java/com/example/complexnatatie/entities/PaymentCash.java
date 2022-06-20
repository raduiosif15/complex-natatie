package com.example.complexnatatie.entities;

import lombok.*;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PaymentCash extends Payment {

    public PaymentCash(int id, Date date, double value, String description, String type, int customerId) {
        super(id, date, value, description, type, customerId);
    }
}
