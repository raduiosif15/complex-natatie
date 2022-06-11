package com.example.complexnatatie.builders;

import com.example.complexnatatie.dtos.TaxDTO;
import com.example.complexnatatie.entities.Tax;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class TaxBuilder {
    public static TaxDTO fromEntity(Tax tax) {
        return TaxDTO.builder()
                .id(tax.getId())
                .description(tax.getDescription())
                .taxType(tax.getTaxType())
                .taxValue(tax.getTaxValue())
                .build();
    }

    public static List<TaxDTO> fromEntities(List<Tax> taxes) {
        return taxes.stream().map(TaxBuilder::fromEntity).collect(Collectors.toList());
    }

    public static Tax fromDTO(TaxDTO taxDTO) {
        return Tax.builder()
                .description(taxDTO.getDescription())
                .taxType(taxDTO.getTaxType())
                .taxValue(taxDTO.getTaxValue())
                .build();
    }
}