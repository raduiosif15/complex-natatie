package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.CustomerType;
import com.example.complexnatatie.dtos.TaxDTO;
import com.example.complexnatatie.entities.Tax;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class TaxBuilder {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static TaxDTO fromEntity(Tax tax) {
        return TaxDTO.builder()
                .id(tax.getId())
                .description(tax.getDescription())
                .type(new CustomerType(tax.getType()))
                .value(Double.parseDouble(df.format(tax.getValue())))
                .build();
    }

    public static List<TaxDTO> fromEntities(List<Tax> taxes) {
        return taxes.stream().map(TaxBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Tax fromDTO(TaxDTO taxDTO) {
        return Tax.builder()
                .description(taxDTO.getDescription())
                .type(taxDTO.getType().getName())
                .value(Double.parseDouble(df.format(taxDTO.getValue())))
                .build();
    }
}
