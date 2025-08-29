package com.ucsal.arqsoftware.servicies;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ucsal.arqsoftware.dto.ApprovalHistoryDTO;
import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.enums.RequestStatus;
import com.ucsal.arqsoftware.entities.User;
import com.ucsal.arqsoftware.repositories.ApprovalHistoryRepository;
import com.ucsal.arqsoftware.repositories.RequestRepository;
import com.ucsal.arqsoftware.repositories.UserRepository;
import com.ucsal.arqsoftware.servicies.exceptions.DatabaseException;
import com.ucsal.arqsoftware.servicies.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class ApprovalHistoryService {

    @Autowired
    private ApprovalHistoryRepository repository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private SchedulerService schedulerService;

    @Transactional(readOnly = true)
    public ApprovalHistoryDTO findById(Long id) {
        ApprovalHistory approvalHistory = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Histórico de aprovação não encontrado"));
        return new ApprovalHistoryDTO(approvalHistory);
    }

    @Transactional(readOnly = true)
    public Page<ApprovalHistoryDTO> findAll(Pageable pageable) {
        Page<ApprovalHistory> result = repository.findAll(pageable);
        return result.map(ApprovalHistoryDTO::new);
    }

    @Transactional
    public ApprovalHistoryDTO insert(ApprovalHistoryDTO dto) throws SchedulerException {
    	ApprovalHistory entity = new ApprovalHistory();
        copyDtoToEntity(dto, entity);
        entity.setDateTime(Date.from(Instant.now()));
        entity = repository.save(entity);

        if (entity.isDecision()) {
            rejectConflictingRequests(entity.getRequest());
            schedulerService.scheduleApprovalHistoryJobs(entity);

        }

        return new ApprovalHistoryDTO(entity);
    }

    @Transactional
    public ApprovalHistoryDTO update(Long id, ApprovalHistoryDTO dto) throws SchedulerException {
        Request request = validateAndGetRequest(dto.getRequestId());
        checkForConflicts(request);

        try {
            ApprovalHistory entity = repository.getReferenceById(id);
            boolean decisionAntes = entity.isDecision();
            boolean decisionDepois = dto.isDecision();

            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            if (!decisionAntes && decisionDepois) {
                rejectConflictingRequests(entity.getRequest());
                schedulerService.scheduleApprovalHistoryJobs(entity);
            }
            return new ApprovalHistoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Histórico de aprovação não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
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
    	
    	User user = userRepository.getReferenceById(dto.getUserId());
    	Request request = requestRepository.getReferenceById(dto.getRequestId());
    	
        entity.setDateTime(dto.getDateTime());
        entity.setDecision(dto.isDecision());
        entity.setObservation(dto.getObservation());
        entity.setUser(user);
        entity.setRequest(request);
        
        request.setStatus(dto.isDecision() ? RequestStatus.APPROVED : RequestStatus.REJECTED);
        requestRepository.save(request);
       
    }

    public Request validateAndGetRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Requisição não encontrada"));
    }

    public void checkForConflicts(Request request) {
        boolean conflito = requestRepository.existsByPhysicalSpaceAndStatusAndDateTimeStartLessThanEqualAndDateTimeEndGreaterThanEqual(
                request.getPhysicalSpace(),
                RequestStatus.APPROVED,
                request.getDateTimeEnd(),
                request.getDateTimeStart()
        );

        if (conflito) {
            throw new DatabaseException("Já existe uma requisição aprovada neste espaço físico nesse horário.");
        }
    }

    public void rejectConflictingRequests(Request entity) {
        List<Request> pendingRequests = requestRepository
                .findByPhysicalSpaceAndStatusAndDateTimeStartLessThanEqualAndDateTimeEndGreaterThanEqual(
                        entity.getPhysicalSpace(),
                        RequestStatus.PENDING,
                        entity.getDateTimeEnd(),
                        entity.getDateTimeStart()
                );

        for (Request r : pendingRequests) {
            if (!r.getId().equals(entity.getId())) {
                r.setStatus(RequestStatus.REJECTED);
                requestRepository.save(r);
            }
        }
    }

}
