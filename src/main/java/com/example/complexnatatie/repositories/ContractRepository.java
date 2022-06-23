package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query(value = "SELECT contract " +
            "FROM Contract contract " +
            "WHERE contract.customerId = :customerId " +
            "AND contract.endDate >= CURRENT_DATE ")
    Optional<Contract> getActiveContractByCustomerId(int customerId);

    @Query(value = "SELECT contract " +
            "FROM Contract contract " +
            "WHERE contract.customerId = :customerId ")
    List<Contract> getAllByCustomerId(int customerId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*), customer_type, to_char(start_date, 'Mon') as mon " +
                    "FROM contract " +
                    "WHERE contract.start_date < CURRENT_DATE " +
                    "  AND contract.end_date > CURRENT_DATE " +
                    "AND extract(year from contract.start_date) = :year " +
                    "group by 2, 3 ")
    List<Object[]> getActiveMonthStatisticForYear(int year);
}
