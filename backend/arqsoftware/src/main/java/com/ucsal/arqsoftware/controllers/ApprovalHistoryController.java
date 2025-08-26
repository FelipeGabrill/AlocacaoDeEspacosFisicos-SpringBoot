package com.ucsal.arqsoftware.controllers;

import java.net.URI;

import com.ucsal.arqsoftware.dto.ApprovalHistoryDTO;
import com.ucsal.arqsoftware.servicies.ApprovalHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/approvalhistories", produces = "application/json")
@Tag(name = "Approval History", description = "Endpoints for managing approval histories")
@SecurityRequirement(name = "bearerAuth")
public class ApprovalHistoryController {

    @Autowired
    private ApprovalHistoryService service;

    @Operation(
            summary = "Get Approval History by ID",
            description = "Retrieve an approval history record by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApprovalHistoryDTO> findById(@PathVariable Long id) {
        ApprovalHistoryDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List all Approval Histories",
            description = "Retrieve all approval history records with pagination support",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<Page<ApprovalHistoryDTO>> findAll(Pageable pageable) {
        Page<ApprovalHistoryDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Create a new Approval History",
            description = "Insert a new approval history record",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<ApprovalHistoryDTO> insert(@Valid @RequestBody ApprovalHistoryDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(
            summary = "Update an Approval History",
            description = "Update an existing approval history record",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApprovalHistoryDTO> update(@PathVariable Long id, @Valid @RequestBody ApprovalHistoryDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete an Approval History",
            description = "Delete an approval history record by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
