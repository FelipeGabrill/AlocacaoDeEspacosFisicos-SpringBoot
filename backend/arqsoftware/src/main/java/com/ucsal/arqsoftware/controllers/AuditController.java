package com.ucsal.arqsoftware.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ucsal.arqsoftware.dto.AuditDTO;
import com.ucsal.arqsoftware.servicies.AuditService;

@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/logs")
    public ResponseEntity<List<AuditDTO>> getAllAuditLogs() {
        List<AuditDTO> auditLogs = auditService.getAllAuditLogs();
        return ResponseEntity.ok(auditLogs);
    }
}
