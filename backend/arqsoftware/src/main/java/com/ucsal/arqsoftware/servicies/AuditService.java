package com.ucsal.arqsoftware.servicies;

import com.ucsal.arqsoftware.queryfilters.AuditQueryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucsal.arqsoftware.dto.AuditDTO;
import com.ucsal.arqsoftware.entities.AuditLog;
import com.ucsal.arqsoftware.repositories.AuditLogRepository;

import java.util.List; 
import java.util.stream.Collectors;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(String username, String action, String details) {
        AuditLog log = new AuditLog(username, action, details);
        auditLogRepository.save(log);
    }

    public Page<AuditDTO> getAllAuditLogs(AuditQueryFilter filter, Pageable pageable) {
        Page<AuditLog> result = auditLogRepository.findAll(filter.toSpecification(), pageable);
        return result.map(AuditDTO::new);
    }
}
