package com.example.complexnatatie.security.payload.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {

    @NotBlank
    private String utcnId;

    @NotBlank
    private String password;

}
