package com.ucsal.arqsoftware.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ucsal.arqsoftware.dto.RequestDTO;
import com.ucsal.arqsoftware.entities.RequestStatus;
import com.ucsal.arqsoftware.servicies.RequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/requests")
public class RequestController {

    @Autowired
    private RequestService service;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<RequestDTO> findById(@PathVariable Long id) {
        RequestDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<Page<RequestDTO>> findAll(Pageable pageable) {
        Page<RequestDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    @PostMapping
    public ResponseEntity<RequestDTO> insert(@Valid @RequestBody RequestDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<RequestDTO> update(@PathVariable Long id, @Valid @RequestBody RequestDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/asc")
    public ResponseEntity<Page<RequestDTO>> getByDataAsc(Pageable pageable){
    	Page<RequestDTO> dto = service.getByDataAsc(pageable);
    	return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/desc")
    public ResponseEntity<Page<RequestDTO>> getByDataDesc(Pageable pageable){
    	Page<RequestDTO> dto = service.getByDataDesc(pageable);
    	return ResponseEntity.ok(dto);
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<RequestDTO>> getByStatusOrderByDateCreationRequestDesc(
            @PathVariable RequestStatus status, Pageable pageable) {
        Page<RequestDTO> dto = service.getByStatus(status, pageable);
        return ResponseEntity.ok(dto);
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<RequestDTO>> getByUserId(
            @PathVariable Long userId, Pageable pageable) {
        Page<RequestDTO> dto = service.getByUserId(userId, pageable);
        return ResponseEntity.ok(dto);
    }
	
}