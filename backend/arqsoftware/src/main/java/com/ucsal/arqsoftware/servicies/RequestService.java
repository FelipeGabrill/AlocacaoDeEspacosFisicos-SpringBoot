package com.ucsal.arqsoftware.servicies;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ucsal.arqsoftware.dto.RequestDTO;
import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.RequestStatus;
import com.ucsal.arqsoftware.entities.User;
import com.ucsal.arqsoftware.repositories.PhysicalSpaceRepository;
import com.ucsal.arqsoftware.repositories.RequestRepository;
import com.ucsal.arqsoftware.repositories.UserRepository;
import com.ucsal.arqsoftware.servicies.exceptions.DatabaseException;
import com.ucsal.arqsoftware.servicies.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PhysicalSpaceRepository physicalSpaceRepository;
    
    @Transactional(readOnly = true)
    public RequestDTO findById(Long id) {
        Request request = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Requisição não encontrada"));
        return new RequestDTO(request);
    }

    @Transactional(readOnly = true)
    public Page<RequestDTO> findAll(Pageable pageable) {
        Page<Request> result = repository.findAll(pageable);
        return result.map(RequestDTO::new);
    }

    @Transactional
    public RequestDTO insert(RequestDTO dto) {
        Request entity = new Request();
        copyDtoToEntity(dto, entity);
        entity.setStatus(RequestStatus.PENDING);
        entity.setDateCreationRequest(Date.from(Instant.now()));
        entity = repository.save(entity);
        return new RequestDTO(entity);
    }

    @Transactional
    public RequestDTO update(Long id, RequestDTO dto) {
        try {
            Request entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new RequestDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Requisição não encontrada");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Requisição não encontrada");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(RequestDTO dto, Request entity) {
    	User user = userRepository.getReferenceById(dto.getUserId());
    	PhysicalSpace physicalSpace = physicalSpaceRepository.getReferenceById(dto.getPhysicalSpaceId());
    	
        entity.setDateTimeStart(dto.getDateTimeStart());
        entity.setDateTimeEnd(dto.getDateTimeEnd());
        entity.setNeeds(dto.getNeeds());
        entity.setTitle(dto.getTitle());  
        entity.setUser(user);
        entity.setPhysicalSpace(physicalSpace);        
    }

    @Transactional(readOnly = true)
	public Page<RequestDTO> getByDataAsc(Pageable pageable) {
		Page<Request> result = repository.findAllByOrderByDateCreationRequestAsc(pageable);
		return result.map(RequestDTO::new);
	}

	@Transactional(readOnly = true)
	public Page<RequestDTO> getByDataDesc(Pageable pageable) {
		Page<Request> result = repository.findAllByOrderByDateCreationRequestDesc(pageable);
		return result.map(RequestDTO::new);	
	}
	
	@Transactional(readOnly = true)
	public Page<RequestDTO> getByStatus(RequestStatus status, Pageable pageable) {
		 Page<Request> result = repository.findAllByStatus(status, pageable);
	     return result.map(RequestDTO::new);
	}

	@Transactional(readOnly = true)
	public Page<RequestDTO> getByUserLogin(String userLogin, Pageable pageable) {
	    User user = userRepository.findByLogin(userLogin)
	        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + userLogin));
	    
	    Long userId = user.getId();
	    Page<Request> requests = repository.findAllByUserId(userId, pageable);
	    return requests.map(RequestDTO::new);
	}

	@Transactional(readOnly = true)
	public Page<RequestDTO> getByTitle(String title, Pageable pageable) {
	   Page<Request> result = repository.findByTitleIgnoreCaseContaining(title, pageable);
	   return result.map(RequestDTO::new);
	}
}