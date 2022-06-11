package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query(value = "SELECT contract " +
            "FROM Contract contract " +
            "JOIN Customer customer ON customer.id = contract.customerId " +
            "WHERE contract.customerId = :customerId " +
            "ORDER BY contract.expirationDate DESC ")
    List<Contract> getContractsByCustomerId(int customerId);

}
