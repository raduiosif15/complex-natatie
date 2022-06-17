package com.example.complexnatatie.security.payload.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private int id;

    private String token;

    private String utcnId;

    private String type;

}
