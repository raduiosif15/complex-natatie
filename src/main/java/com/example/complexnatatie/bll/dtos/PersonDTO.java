package com.example.complexnatatie.bll.dtos;

import com.example.complexnatatie.dao.models.Person;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static PersonDTO fromPerson(Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .phone(person.getPhone())
                .build();
    }

    public static List<PersonDTO> fromPersons(List<Person> persons) {
        return persons.stream().map(PersonDTO::fromPerson).collect(Collectors.toList());
    }

}
