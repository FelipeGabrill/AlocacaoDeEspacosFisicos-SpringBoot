package com.ucsal.arqsoftware.servicies;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<AuditDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(AuditDTO::new)
                .collect(Collectors.toList());
    }
}
