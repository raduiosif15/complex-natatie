package com.example.complexnatatie.security.payload.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private int id;

    private String token;

    private String type = "Bearer";

    private String utcnId;

    private List<String> roles;

}
