package com.ucsal.arqsoftware.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ucsal.arqsoftware.entities.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}