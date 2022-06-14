package com.example.complexnatatie.repositories;

import com.example.complexnatatie.builders.helpers.ERole;
import com.example.complexnatatie.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);

}
