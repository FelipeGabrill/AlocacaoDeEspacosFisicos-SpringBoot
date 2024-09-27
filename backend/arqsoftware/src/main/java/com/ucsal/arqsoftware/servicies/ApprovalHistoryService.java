package com.ucsal.arqsoftware.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ucsal.arqsoftware.dto.ApprovalHistoryDTO;
import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.repositories.ApprovalHistoryRepository;
import com.ucsal.arqsoftware.servicies.exceptions.DatabaseException;
import com.ucsal.arqsoftware.servicies.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ApprovalHistoryService {

    @Autowired
    private ApprovalHistoryRepository repository;

    public ApprovalHistoryDTO findById(Long id) {
        ApprovalHistory approvalHistory = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Histórico de aprovação não encontrado"));
        return new ApprovalHistoryDTO(approvalHistory);
    }

    public Page<ApprovalHistoryDTO> findAll(Pageable pageable) {
        Page<ApprovalHistory> result = repository.findAll(pageable);
        return result.map(ApprovalHistoryDTO::new);
    }

    public ApprovalHistoryDTO insert(ApprovalHistoryDTO dto) {
        ApprovalHistory entity = new ApprovalHistory();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ApprovalHistoryDTO(entity);
    }

    public ApprovalHistoryDTO update(Long id, ApprovalHistoryDTO dto) {
        try {
            ApprovalHistory entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ApprovalHistoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Histórico de aprovação não encontrado");
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Histórico de aprovação não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(ApprovalHistoryDTO dto, ApprovalHistory entity) {
        entity.setDateTime(dto.getDateTime());
        entity.setDecision(dto.isDecision());
        entity.setObservation(dto.getObservation());
        // Associações de User e Requests podem ser feitas aqui
    }
}
