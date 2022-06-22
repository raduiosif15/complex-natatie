package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "SELECT customer " +
            "FROM Customer customer " +
            "WHERE LOWER(CONCAT(customer.firstName, ' ', customer.lastName)) LIKE %:name% " +
            "OR LOWER(CONCAT(customer.lastName, ' ', customer.firstName)) LIKE %:name% ")
    List<Customer> getByName(String name);

    @Query(value = "SELECT customer " +
            "FROM Customer customer " +
            "WHERE customer.codeID = :codeID ")
    Optional<Customer> getByCodeID(long codeID);

}
