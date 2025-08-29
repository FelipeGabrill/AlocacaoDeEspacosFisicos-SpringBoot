package com.ucsal.arqsoftware.servicies;

import com.ucsal.arqsoftware.dto.PhysicalSpaceSimpleDTO;
import com.ucsal.arqsoftware.queryfilters.PhysicalSpaceQueryFilter;
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
import com.ucsal.arqsoftware.entities.enums.PhysicalSpaceType;
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
	public PhysicalSpaceSimpleDTO findById(Long id) {
		PhysicalSpace physicalSpace = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new PhysicalSpaceSimpleDTO(physicalSpace);
	}
	
	@Transactional(readOnly = true)
	public Page<PhysicalSpaceSimpleDTO> findAll(PhysicalSpaceQueryFilter filter, Pageable pageable) {
		Page<PhysicalSpace> result = repository.findAll(filter.toSpecification(),pageable);
		return result.map(x -> new PhysicalSpaceSimpleDTO(x));
	}

    @Transactional(readOnly = true)
    public Page<PhysicalSpaceDTO> findByAllAndRequests(PhysicalSpaceQueryFilter filter, Pageable pageable) {
        Page<PhysicalSpace> result = repository.findAll(filter.toSpecification(),pageable);
        return result.map(x -> new PhysicalSpaceDTO(x));
    }
	
	@Transactional
	public PhysicalSpaceSimpleDTO insert(PhysicalSpaceSimpleDTO dto) {
		PhysicalSpace entity = new PhysicalSpace();
		copyDtoToEntityForInsert(dto, entity);
		entity.setAvailability(true);
		entity = repository.save(entity);
		return new PhysicalSpaceSimpleDTO(entity);
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

	private void copyDtoToEntityForInsert(PhysicalSpaceSimpleDTO dto, PhysicalSpace entity) {
		entity.setName(dto.getName());
		entity.setLocation(dto.getLocation());
		entity.setCapacity(dto.getCapacity());
		entity.setType(dto.getType());
		entity.setResources(dto.getResources());
		entity.setAvailability(dto.getAvailability());
	}

    private void copyDtoToEntity(PhysicalSpaceDTO dto, PhysicalSpace entity) {
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
        entity.setCapacity(dto.getCapacity());
        entity.setType(dto.getType());
        entity.setResources(dto.getResources());
        entity.setAvailability(dto.getAvailability());
        for (RequestDTO reqDto : dto.getRequests()) {
            boolean exists = entity.getRequests().stream()
                    .anyMatch(req -> req.getId().equals(reqDto.getId()));
            if (!exists) {
                Request req = new Request();
                req.setId(reqDto.getId());
                entity.getRequests().add(req);
            }
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
