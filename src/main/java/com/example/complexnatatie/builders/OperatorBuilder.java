package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.OperatorType;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.entities.Operator;
import com.example.complexnatatie.security.service.UserDetailsImpl;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class OperatorBuilder {
    public static OperatorDTO fromEntity(Operator operator) {
        return OperatorDTO.builder()
                .id(operator.getId())
                .utcnId(operator.getUtcnId())
                .operatorType(new OperatorType(operator.getOperatorType()))
                .build();
    }

    public static List<OperatorDTO> fromEntities(List<Operator> operators) {
        return operators.stream().map(OperatorBuilder::fromEntity).collect(Collectors.toList());
    }

    public static UserDetailsImpl userDetailsBuilder(Operator operator) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(operator.getOperatorType()));

        return UserDetailsImpl.builder()
                .username(operator.getUtcnId())
                .password(operator.getPassword())
                .authorities(authorities)
                .build();
    }
}
