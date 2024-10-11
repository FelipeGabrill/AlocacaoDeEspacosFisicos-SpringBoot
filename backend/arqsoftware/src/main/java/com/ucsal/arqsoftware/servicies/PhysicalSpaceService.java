package com.ucsal.arqsoftware.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ucsal.arqsoftware.dto.PhysicalSpaceDTO;
import com.ucsal.arqsoftware.dto.RequestDTO;
import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.PhysicalSpaceType;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.repositories.PhysicalSpaceRepository;
import com.ucsal.arqsoftware.servicies.exceptions.DatabaseException;
import com.ucsal.arqsoftware.servicies.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PhysicalSpaceService {

	@Autowired
	private PhysicalSpaceRepository repository;
	
	@Transactional(readOnly = true)
	public PhysicalSpaceDTO findById(Long id) {
		PhysicalSpace physicalSpace = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new PhysicalSpaceDTO(physicalSpace);
	}
	
	@Transactional(readOnly = true)
	public Page<PhysicalSpaceDTO> findAll(Pageable pageable) {
		Page<PhysicalSpace> result = repository.findAll(pageable);
		return result.map(x -> new PhysicalSpaceDTO(x));
	}
	
	@Transactional
	public PhysicalSpaceDTO insert(PhysicalSpaceDTO dto) {
		PhysicalSpace entity = new PhysicalSpace();
		copyDtoToEntity(dto, entity);
		entity.setAvailability(true);
		entity = repository.save(entity);
		return new PhysicalSpaceDTO(entity);
	}
	
	@Transactional
	public PhysicalSpaceDTO update(Long id, PhysicalSpaceDTO dto) {
		try {
			PhysicalSpace entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new PhysicalSpaceDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

    @Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
	        repository.deleteById(id);    		
		}
	    catch (DataIntegrityViolationException e) {
	        throw new DatabaseException("Falha de integridade referencial");
	   	}
	}

	private void copyDtoToEntity(PhysicalSpaceDTO dto, PhysicalSpace entity) {
		entity.setName(dto.getName());
		entity.setLocation(dto.getLocation());
		entity.setCapacity(dto.getCapacity());
		entity.setType(dto.getType());
		entity.setResources(dto.getResources());
		entity.setAvailability(dto.getAvailability());
		entity.getRequests().clear();
		for (RequestDTO reqDto : dto.getRequests()) {
			Request req = new Request();
			req.setId(reqDto.getId());
			entity.getRequests().add(req);
		}
	}

	@Transactional(readOnly = true)
	public Page<PhysicalSpaceDTO> getByType(PhysicalSpaceType type, Pageable pageable) {
		Page<PhysicalSpace> result = repository.findAllByType(type, pageable);
	    return result.map(PhysicalSpaceDTO::new);
	}
	
	@Transactional(readOnly = true)
	public Page<PhysicalSpaceDTO> getByCapacity(Integer capacity, Pageable pageable) {
	    Page<PhysicalSpace> result = repository.findAllByCapacity(capacity, pageable);
	    return result.map(PhysicalSpaceDTO::new);
	}
	
	@Transactional(readOnly = true)
	public Page<PhysicalSpaceDTO> getByName(String name, Pageable pageable) {
        Page<PhysicalSpace> result = repository.findAllByNameContainingIgnoreCase(name, pageable);
        return result.map(PhysicalSpaceDTO::new);
    }
	
	@Transactional(readOnly = true)
	public Page<PhysicalSpaceDTO> getByAvailability(Boolean availability, Pageable pageable) {
	    Page<PhysicalSpace> result = repository.findAllByAvailability(availability, pageable);
	    return result.map(PhysicalSpaceDTO::new);
    }
}
