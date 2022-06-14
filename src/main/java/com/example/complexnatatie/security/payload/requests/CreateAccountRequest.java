package com.example.complexnatatie.security.payload.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateAccountRequest {

    @NotBlank
    private String utcnId;

    @NotBlank
    private String password;

    private Set<String> role;

}
