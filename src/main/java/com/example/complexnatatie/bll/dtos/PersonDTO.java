package com.example.complexnatatie.bll.dtos;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PersonDTO {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;



}
