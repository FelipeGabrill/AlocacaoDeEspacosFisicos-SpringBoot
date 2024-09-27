package com.ucsal.arqsoftware.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ucsal.arqsoftware.dto.RequestDTO;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.repositories.RequestRepository;
import com.ucsal.arqsoftware.servicies.exceptions.DatabaseException;
import com.ucsal.arqsoftware.servicies.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repository;

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
        entity.setDateTimeStart(dto.getDateTimeStart());
        entity.setDateTimeEnd(dto.getDateTimeEnd());
        entity.setDateCreationRequest(dto.getDateCreationRequest());
        entity.setNeeds(dto.getNeeds());
        entity.setStatus(dto.getStatus());
        
    }
}