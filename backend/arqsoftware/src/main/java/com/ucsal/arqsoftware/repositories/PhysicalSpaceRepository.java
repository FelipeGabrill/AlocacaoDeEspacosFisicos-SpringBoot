package com.ucsal.arqsoftware.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ucsal.arqsoftware.entities.PhysicalSpace;

@Repository
public interface PhysicalSpaceRepository extends JpaRepository<PhysicalSpace, Long>  {

}
