package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    @Query(value = "SELECT p " +
            "FROM Person p " +
            "WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE %:name% " +
            "OR LOWER(CONCAT(p.lastName, ' ', p.firstName)) LIKE %:name% ")
    public List<Person> getByName(String name);

}
