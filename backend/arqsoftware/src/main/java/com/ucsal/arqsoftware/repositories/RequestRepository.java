package com.ucsal.arqsoftware.repositories;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.RequestStatus;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {

	Page<Request> findAllByOrderByDateCreationRequestAsc(Pageable pageable);

	Page<Request> findAllByOrderByDateCreationRequestDesc(Pageable pageable);
	
	Page<Request> findAllByStatus(RequestStatus status, Pageable pageable);
	
    Page<Request> findAllByUserId(Long userId, Pageable pageable);

    Page<Request> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

    boolean existsByPhysicalSpaceAndStatusAndDateTimeStartLessThanEqualAndDateTimeEndGreaterThanEqual(
            PhysicalSpace physicalSpace,
            RequestStatus status,
            Date dateTimeEnd,
            Date dateTimeStart
    );

    List<Request> findByPhysicalSpaceAndStatusAndDateTimeStartLessThanEqualAndDateTimeEndGreaterThanEqual(
            PhysicalSpace physicalSpace,
            RequestStatus requestStatus,
            Date dateTimeEnd,
            Date dateTimeStart);
}