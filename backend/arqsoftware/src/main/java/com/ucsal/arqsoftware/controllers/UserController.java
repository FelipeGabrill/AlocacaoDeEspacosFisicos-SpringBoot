package com.ucsal.arqsoftware.controllers;

import java.net.URI;

import com.ucsal.arqsoftware.dto.UserDTO;
import com.ucsal.arqsoftware.dto.UserInsertDTO;
import com.ucsal.arqsoftware.dto.UserUpdateDTO;
import com.ucsal.arqsoftware.servicies.UserService;

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

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
@Tag(name = "Users", description = "Endpoints for managing users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(
            summary = "Get Logged-in User",
            description = "Retrieve the details of the currently authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        UserDTO dto = service.getMe();
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get User by ID",
            description = "Retrieve a user by their unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "List all Users",
            description = "Retrieve all users with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> findByAll(Pageable pageable) {
        Page<UserDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Create a new User",
            description = "Register a new user in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {
        UserDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @Operation(
            summary = "Update a User",
            description = "Update the details of an existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        UserDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @Operation(
            summary = "Delete a User",
            description = "Delete a user by their unique identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "List Users by Login",
            description = "Retrieve users filtered by login with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/login/{login}")
    public ResponseEntity<Page<UserDTO>> getByLogin(
            @PathVariable String login, Pageable pageable) {
        Page<UserDTO> users = service.getByLogin(login, pageable);
        return ResponseEntity.ok(users);
    }
}
