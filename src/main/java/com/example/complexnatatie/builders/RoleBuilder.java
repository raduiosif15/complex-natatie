package com.example.complexnatatie.builders;

import com.example.complexnatatie.dtos.RoleDTO;
import com.example.complexnatatie.entities.Role;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class RoleBuilder {
    public static RoleDTO fromEntity(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static List<RoleDTO> fromEntities(List<Role> roles) {
        return roles.stream().map(RoleBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Role fromDTO(RoleDTO roleDTO) {
        return Role.builder()
                .name(roleDTO.getName())
                .build();
    }
}
