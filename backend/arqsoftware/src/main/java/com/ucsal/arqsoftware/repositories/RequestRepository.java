package com.ucsal.arqsoftware.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.RequestStatus;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

	Page<Request> findAllByOrderByDateCreationRequestAsc(Pageable pageable);

	Page<Request> findAllByOrderByDateCreationRequestDesc(Pageable pageable);
	
	Page<Request> findAllByStatus(RequestStatus status, Pageable pageable);
	
    Page<Request> findAllByUserId(Long userId, Pageable pageable);

}