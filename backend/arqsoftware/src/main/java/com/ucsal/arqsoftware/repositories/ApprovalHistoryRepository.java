package com.ucsal.arqsoftware.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ucsal.arqsoftware.entities.ApprovalHistory;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {
}