package com.ucsal.arqsoftware.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ucsal.arqsoftware.dto.ApprovalHistoryDTO;
import com.ucsal.arqsoftware.dto.RequestDTO;
import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.Request;
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
    
    public RequestDTO findById(Long id) {
        Request request = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Requisição não encontrada"));
        return new RequestDTO(request);
    }

    public Page<RequestDTO> findAll(Pageable pageable) {
        Page<Request> result = repository.findAll(pageable);
        return result.map(RequestDTO::new);
    }

    public RequestDTO insert(RequestDTO dto) {
        Request entity = new Request();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new RequestDTO(entity);
    }

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
        entity.setDateCreationRequest(dto.getDateCreationRequest());
        entity.setNeeds(dto.getNeeds());
        entity.setStatus(dto.getStatus());   
        entity.setUser(user);
        entity.setPhysicalSpace(physicalSpace);
        entity.getApprovalHistories().clear();
        for (ApprovalHistoryDTO aprDto : dto.getApprovalHistories()) {
        	ApprovalHistory apr = new ApprovalHistory();
        	apr.setId(aprDto.getId());
        	entity.getApprovalHistories().add(apr);
        }
    }
}