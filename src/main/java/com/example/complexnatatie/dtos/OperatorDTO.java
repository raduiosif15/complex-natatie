package com.example.complexnatatie.dtos;

import com.example.complexnatatie.builders.helpers.OperatorType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OperatorDTO {

    private int id;

    private String utcnId;

    private String password;

    private OperatorType operatorType;

}
