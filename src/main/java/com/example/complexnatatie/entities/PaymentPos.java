package com.example.complexnatatie.entities;

import lombok.*;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PaymentPos extends Payment {
    public PaymentPos(int id, Date date, double value, String description, String type, int customerId) {
        super(id, date, value, description, type, customerId);
    }
}
