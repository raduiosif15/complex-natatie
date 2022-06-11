package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    @Query(value = "SELECT c " +
            "FROM Contract c " +
            "JOIN Person p ON p.id = c.clientId " +
            "WHERE c.clientId = :clientId " +
            "ORDER BY c.expirationDate DESC ")
    List<Contract> getContractsByClientId(UUID clientId);

}
