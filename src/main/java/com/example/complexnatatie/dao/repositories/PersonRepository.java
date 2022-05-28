package com.example.complexnatatie.dao.repositories;

import com.example.complexnatatie.dao.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
}
