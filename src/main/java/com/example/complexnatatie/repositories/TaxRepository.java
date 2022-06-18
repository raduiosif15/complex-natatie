package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TaxRepository extends JpaRepository<Tax, Integer> {

    @Query(value = "SELECT tax " +
            "FROM Tax tax " +
            "WHERE tax.type = :customerType ")
    Optional<Tax> findByType(String customerType);

}
