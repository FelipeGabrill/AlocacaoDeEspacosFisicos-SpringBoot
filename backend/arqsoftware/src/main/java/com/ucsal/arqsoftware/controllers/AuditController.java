package com.ucsal.arqsoftware.controllers;

import java.util.List;

import com.ucsal.arqsoftware.dto.AuditDTO;
import com.ucsal.arqsoftware.servicies.AuditService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/audit", produces = "application/json")
@Tag(name = "Audit", description = "Endpoints for auditing logs")
@SecurityRequirement(name = "bearerAuth")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Operation(
            summary = "Get All Audit Logs",
            description = "Retrieve a list of all audit logs recorded in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK – List of audit logs returned")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/logs")
    public ResponseEntity<List<AuditDTO>> getAllAuditLogs() {
        List<AuditDTO> auditLogs = auditService.getAllAuditLogs();
        return ResponseEntity.ok(auditLogs);
    }
}
