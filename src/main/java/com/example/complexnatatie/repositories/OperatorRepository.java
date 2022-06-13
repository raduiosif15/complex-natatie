package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Operator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Integer> {

    // todo: implement this
    Optional<Operator> findByUtcnId(String utcnId);


}
