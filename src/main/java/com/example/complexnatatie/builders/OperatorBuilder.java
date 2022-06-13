package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.OperatorType;
import com.example.complexnatatie.dtos.OperatorDTO;
import com.example.complexnatatie.entities.Operator;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class OperatorBuilder {
    public static OperatorDTO fromEntity(Operator operator) {
        return OperatorDTO.builder()
                .id(operator.getId())
                .utcnId(operator.getUtcnId())
                .operatorType(new OperatorType(operator.getOperatorType()))
                .password(operator.getPassword())
                .build();
    }

    public static List<OperatorDTO> fromEntities(List<Operator> operators) {
        return operators.stream().map(OperatorBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Operator fromDTO(OperatorDTO operatorDTO) {
        return Operator.builder()
                .utcnId(operatorDTO.getUtcnId())
                .operatorType(operatorDTO.getOperatorType().getName())
                .password(operatorDTO.getPassword())
                .build();
    }
}
