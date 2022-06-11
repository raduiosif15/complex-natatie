package com.example.complexnatatie.entities;

import com.example.complexnatatie.dtos.PersonDTO;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Person {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "cnp")
    private String cnp;

    public static Person fromPersonDTO(PersonDTO personDTO) {
        return Person.builder()
                .firstName(personDTO.getFirstName())
                .lastName(personDTO.getLastName())
                .email(personDTO.getEmail())
                .phone(personDTO.getPhone())
                .cnp(personDTO.getCnp())
                .build();
    }
}
