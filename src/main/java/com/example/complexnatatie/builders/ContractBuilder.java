package com.example.complexnatatie.builders;

import com.example.complexnatatie.builders.helpers.CustomerType;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.ContractMonthlyStatistic;
import com.example.complexnatatie.dtos.PaymentMonthlyStatistic;
import com.example.complexnatatie.entities.Contract;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ContractBuilder {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static ContractDTO fromEntity(Contract contract) {
        return ContractDTO.builder()
                .id(contract.getId())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .monthly(Double.parseDouble(df.format(contract.getMonthly())))
                .total(Double.parseDouble(df.format(contract.getTotal())))
                .customerType(new CustomerType(contract.getCustomerType()))
                .customerId(contract.getCustomerId())
                .build();
    }

    public static List<ContractDTO> fromEntities(List<Contract> contracts) {
        //noinspection ComparatorMethodParameterNotUsed
        return contracts
                .stream()
                .map(ContractBuilder::fromEntity)
                .sorted((c1, c2) -> c1.getStartDate().after(c2.getEndDate()) ? -1 : 1)
                .collect(Collectors.toList());
    }

    public static Contract fromDTO(ContractDTO contractDTO) {
        return Contract.builder()
                .id(contractDTO.getId())
                .startDate(contractDTO.getStartDate())
                .endDate(contractDTO.getEndDate())
                .monthly(Double.parseDouble(df.format(contractDTO.getMonthly())))
                .total(Double.parseDouble(df.format(contractDTO.getTotal())))
                .customerType(contractDTO.getCustomerType().getName())
                .customerId(contractDTO.getCustomerId())
                .build();
    }

    public static List<ContractMonthlyStatistic> fromStatisticObjects(List<Object[]> statistics) {
        final List<ContractMonthlyStatistic> contractMonthlyStatistics = new ArrayList<>();

        for (Object[] statistic : statistics) {
            final ContractMonthlyStatistic contractMonthlyStatistic = new ContractMonthlyStatistic();
            contractMonthlyStatistic.setCount((BigInteger) statistic[0]);
            contractMonthlyStatistic.setType(new CustomerType((String) statistic[1]));
            contractMonthlyStatistic.setMonth((String) statistic[2]);
            contractMonthlyStatistics.add(contractMonthlyStatistic);
        }

        return contractMonthlyStatistics;
    }

}
