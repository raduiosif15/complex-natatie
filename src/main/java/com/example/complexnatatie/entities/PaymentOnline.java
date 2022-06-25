package com.example.complexnatatie.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PaymentOnline extends Payment {
    public PaymentOnline(int id, Date date, double value, String description, String type, int customerId) {
        super(id, date, value, description, type, customerId);
    }
}
