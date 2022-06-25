package com.example.complexnatatie.security.payload.responses;

import com.example.complexnatatie.dtos.CustomerDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtCustomerResponse {

    private String token;

    private CustomerDTO customer;

}
