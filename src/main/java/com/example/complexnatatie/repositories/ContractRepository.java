package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query(value = "SELECT contract " +
            "FROM Contract contract " +
            "WHERE contract.customerId = :customerId " +
            "AND contract.endDate >= CURRENT_DATE ")
    Optional<Contract> getActiveContractByCustomerId(int customerId);

    @Query("SELECT contract " +
            "FROM Contract contract " +
            "WHERE contract.id = :id ")
    Optional<Contract> getById(int id);

}
