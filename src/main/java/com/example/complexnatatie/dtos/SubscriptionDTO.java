package com.example.complexnatatie.dtos;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SubscriptionDTO {

    private int id;

    private Date startDate;

    private Date endDate;

    private int customerId;

}
