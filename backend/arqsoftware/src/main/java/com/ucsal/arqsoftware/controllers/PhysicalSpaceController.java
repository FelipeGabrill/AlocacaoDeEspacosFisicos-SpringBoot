package com.ucsal.arqsoftware.controllers;

import java.net.URI;

import com.ucsal.arqsoftware.dto.PhysicalSpaceSimpleDTO;
import com.ucsal.arqsoftware.queryfilters.PhysicalSpaceQueryFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ucsal.arqsoftware.dto.PhysicalSpaceDTO;
import com.ucsal.arqsoftware.entities.enums.PhysicalSpaceType;
import com.ucsal.arqsoftware.servicies.PhysicalSpaceService;

import jakarta.validation.Valid;

@Tag(name = "Physical Space", description = "Endpoints for managing physical spaces")
@RestController
@RequestMapping(value = "/physicalspaces", produces = "application/json")
@SecurityRequirement(name = "bearerAuth")
public class PhysicalSpaceController {

    @Autowired
    private PhysicalSpaceService service;

    @Operation(
            summary = "Get Physical Space by ID",
            description = "Retrieve a physical space by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<PhysicalSpaceSimpleDTO> findById(@PathVariable Long id) {
        PhysicalSpaceSimpleDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List all Physical Spaces",
            description = "Retrieve all physical spaces with optional filters and pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<Page<PhysicalSpaceSimpleDTO>> findByAll(@ParameterObject PhysicalSpaceQueryFilter filter, Pageable pageable) {
        Page<PhysicalSpaceSimpleDTO> dto = service.findAll(filter, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List all Physical Spaces and requests",
            description = "Retrieve all physical spaces with optional filters and pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/{id}/requests")
    public ResponseEntity<Page<PhysicalSpaceDTO>> findByAllAndRequests(@ParameterObject PhysicalSpaceQueryFilter filter, Pageable pageable) {
        Page<PhysicalSpaceDTO> dto = service.findByAllAndRequests(filter, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Create a new Physical Space",
            description = "Register a new physical space in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PhysicalSpaceSimpleDTO> insert(@Valid @RequestBody PhysicalSpaceSimpleDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(
            summary = "Update a Physical Space",
            description = "Update the details of an existing physical space",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PhysicalSpaceDTO> update(@PathVariable Long id, @Valid @RequestBody PhysicalSpaceDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete a Physical Space",
            description = "Delete a physical space by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get Physical Spaces by Type",
            description = "Retrieve physical spaces filtered by type",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/type/{type}")
    public ResponseEntity<Page<PhysicalSpaceDTO>> getByType(
            @PathVariable PhysicalSpaceType type, Pageable pageable) {
        Page<PhysicalSpaceDTO> dto = service.getByType(type, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get Physical Spaces by Capacity",
            description = "Retrieve physical spaces filtered by capacity",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<Page<PhysicalSpaceDTO>> getByCapacity(
            @PathVariable Integer capacity, Pageable pageable) {
        Page<PhysicalSpaceDTO> dto = service.getByCapacity(capacity, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get Physical Spaces by Name",
            description = "Retrieve physical spaces filtered by name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/name/{name}")
    public ResponseEntity<Page<PhysicalSpaceDTO>> getByName(
            @PathVariable String name, Pageable pageable) {
        Page<PhysicalSpaceDTO> spaces = service.getByName(name, pageable);
        return ResponseEntity.ok(spaces);
    }

    @Operation(
            summary = "Get Physical Spaces by Availability",
            description = "Retrieve physical spaces filtered by availability (true or false)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/availability/{availability}")
    public ResponseEntity<Page<PhysicalSpaceDTO>> getByAvailability(
            @PathVariable Boolean availability, Pageable pageable) {
        Page<PhysicalSpaceDTO> spaces = service.getByAvailability(availability, pageable);
        return ResponseEntity.ok(spaces);
    }
}
