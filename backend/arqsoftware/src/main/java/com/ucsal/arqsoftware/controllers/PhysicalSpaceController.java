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

import com.ucsal.arqsoftware.dto.PhysicalSpaceDTO;
import com.ucsal.arqsoftware.entities.PhysicalSpaceType;
import com.ucsal.arqsoftware.servicies.PhysicalSpaceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/physicalspaces")
public class PhysicalSpaceController {

	@Autowired
	private PhysicalSpaceService service;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<PhysicalSpaceDTO> findById(@PathVariable Long id) {
		PhysicalSpaceDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
	@GetMapping
	public ResponseEntity<Page<PhysicalSpaceDTO>> findByAll(Pageable pageable) {
		Page<PhysicalSpaceDTO> dto = service.findAll(pageable);
		return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<PhysicalSpaceDTO> insert(@Valid @RequestBody PhysicalSpaceDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<PhysicalSpaceDTO> update(@PathVariable Long id, @Valid @RequestBody PhysicalSpaceDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
	@GetMapping("/type/{type}")
    public ResponseEntity<Page<PhysicalSpaceDTO>> getByType(
            @PathVariable PhysicalSpaceType type, Pageable pageable) {
        Page<PhysicalSpaceDTO> dto = service.getByType(type, pageable);
        return ResponseEntity.ok(dto);
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<Page<PhysicalSpaceDTO>> getByCapacity(
            @PathVariable Integer capacity, Pageable pageable) {
        Page<PhysicalSpaceDTO> dto = service.getByCapacity(capacity, pageable);
        return ResponseEntity.ok(dto);
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_MANAGER')")
    @GetMapping("/name/{name}")
    public ResponseEntity<Page<PhysicalSpaceDTO>> getByName(
            @PathVariable String name, Pageable pageable) {
        Page<PhysicalSpaceDTO> spaces = service.getByName(name, pageable);
        return ResponseEntity.ok(spaces);
    }
}
