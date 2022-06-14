package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Integer> {

    @Query(value = "SELECT o " +
            "FROM Operator o " +
            "WHERE o.utcnId = :utcnId ")
    Optional<Operator> findByUtcnId(String utcnId);


}
