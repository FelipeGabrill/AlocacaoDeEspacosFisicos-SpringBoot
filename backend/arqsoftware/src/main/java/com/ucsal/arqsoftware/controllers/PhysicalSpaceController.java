package com.ucsal.arqsoftware.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import com.ucsal.arqsoftware.servicies.PhysicalSpaceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/physicalspaces")
public class PhysicalSpaceController {

	@Autowired
	private PhysicalSpaceService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<PhysicalSpaceDTO> findById(@PathVariable Long id) {
		PhysicalSpaceDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping
	public ResponseEntity<Page<PhysicalSpaceDTO>> findByAll(Pageable pageable) {
		Page<PhysicalSpaceDTO> dto = service.findAll(pageable);
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping
	public ResponseEntity<PhysicalSpaceDTO> insert(@Valid @RequestBody PhysicalSpaceDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<PhysicalSpaceDTO> update(@PathVariable Long id, @Valid @RequestBody PhysicalSpaceDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
