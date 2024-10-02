package com.ucsal.arqsoftware.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ucsal.arqsoftware.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
