package com.ucsal.arqsoftware.controllers;

import java.net.URI;

import com.ucsal.arqsoftware.queryfilters.RequestQueryFilter;
import com.ucsal.arqsoftware.dto.RequestDTO;
import com.ucsal.arqsoftware.entities.enums.RequestStatus;
import com.ucsal.arqsoftware.servicies.RequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/requests", produces = "application/json")
@Tag(name = "Requests", description = "Endpoints for managing requests")
@SecurityRequirement(name = "bearerAuth")
public class RequestController {

    @Autowired
    private RequestService service;

    @Operation(
            summary = "Get Request by ID",
            description = "Retrieve a request by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> findById(@PathVariable Long id) {
        RequestDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List all Requests",
            description = "Retrieve all requests with optional filters and pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<Page<RequestDTO>> findAll(RequestQueryFilter filter, Pageable pageable) {
        Page<RequestDTO> dto = service.findAll(filter, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Create a new Request",
            description = "Register a new request in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    @PostMapping
    public ResponseEntity<RequestDTO> insert(@Valid @RequestBody RequestDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(
            summary = "Update a Request",
            description = "Update the details of an existing request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<RequestDTO> update(@PathVariable Long id, @Valid @RequestBody RequestDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete a Request",
            description = "Delete a request by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "List Requests by Date Ascending",
            description = "Retrieve requests ordered by creation date ascending",
            responses = { @ApiResponse(responseCode = "200", description = "OK") }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/asc")
    public ResponseEntity<Page<RequestDTO>> getByDataAsc(Pageable pageable){
        Page<RequestDTO> dto = service.getByDataAsc(pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List Requests by Date Descending",
            description = "Retrieve requests ordered by creation date descending",
            responses = { @ApiResponse(responseCode = "200", description = "OK") }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/desc")
    public ResponseEntity<Page<RequestDTO>> getByDataDesc(Pageable pageable){
        Page<RequestDTO> dto = service.getByDataDesc(pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List Requests by Status",
            description = "Retrieve requests filtered by status ordered by creation date descending",
            responses = { @ApiResponse(responseCode = "200", description = "OK") }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<RequestDTO>> getByStatusOrderByDateCreationRequestDesc(
            @PathVariable RequestStatus status,Pageable pageable) {
        Page<RequestDTO> dto = service.getByStatus(status, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List Requests by User Login",
            description = "Retrieve requests filtered by user login",
            responses = { @ApiResponse(responseCode = "200", description = "OK") }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/user/{userLogin}")
    public ResponseEntity<Page<RequestDTO>> getByUserLogin(
            @PathVariable String userLogin,  Pageable pageable) {
        Page<RequestDTO> dto = service.getByUserLogin(userLogin, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List Requests by Title",
            description = "Retrieve requests filtered by title",
            responses = { @ApiResponse(responseCode = "200", description = "OK") }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/title/{title}")
    public ResponseEntity<Page<RequestDTO>> getByTitle(
            @PathVariable String title, Pageable pageable) {
        Page<RequestDTO> requests = service.getByTitle(title, pageable);
        return ResponseEntity.ok(requests);
    }
}
