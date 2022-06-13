package com.example.complexnatatie.repositories;

import com.example.complexnatatie.entities.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<Tax, Integer> {
}
