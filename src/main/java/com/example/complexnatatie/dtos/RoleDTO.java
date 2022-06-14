package com.example.complexnatatie.dtos;

import com.example.complexnatatie.builders.helpers.ERole;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoleDTO {

    private int id;

    private ERole name;

}
