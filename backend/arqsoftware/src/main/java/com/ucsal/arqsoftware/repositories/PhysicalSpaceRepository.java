package com.ucsal.arqsoftware.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.PhysicalSpaceType;

@Repository
public interface PhysicalSpaceRepository extends JpaRepository<PhysicalSpace, Long> , JpaSpecificationExecutor<PhysicalSpace> {

	Page<PhysicalSpace> findAllByType(PhysicalSpaceType type, Pageable pageable);
	
    Page<PhysicalSpace> findAllByCapacity(Integer capacity, Pageable pageable);
    
    Page<PhysicalSpace> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<PhysicalSpace> findAllByAvailability(Boolean availability, Pageable pageable);

}
