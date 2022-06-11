package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "SELECT c " +
            "FROM Customer c " +
            "WHERE LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE %:name% " +
            "OR LOWER(CONCAT(c.lastName, ' ', c.firstName)) LIKE %:name% ")
    List<Customer> getByName(String name);

}
