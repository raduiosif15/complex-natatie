package com.example.complexnatatie.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CustomerDTO {

    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String cnp;

}
