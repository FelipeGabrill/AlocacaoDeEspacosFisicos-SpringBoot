package com.ucsal.arqsoftware.servicies;

import com.ucsal.arqsoftware.job.MarkAvailableJob;
import com.ucsal.arqsoftware.job.MarkUnavailableJob;
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
    private Scheduler scheduler;

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
            List<Request> pendingRequests = requestRepository
                    .findByPhysicalSpaceAndStatusAndDateTimeStartLessThanEqualAndDateTimeEndGreaterThanEqual(
                            entity.getRequest().getPhysicalSpace(),
                            RequestStatus.PENDING,
                            entity.getRequest().getDateTimeEnd(),
                            entity.getRequest().getDateTimeStart()
                    );


            for (Request r : pendingRequests) {
                if (!r.getId().equals(entity.getRequest().getId())) {
                    r.setStatus(RequestStatus.REJECTED);
                }
            }

            requestRepository.saveAll(pendingRequests);

            JobDetail jobInicio = JobBuilder.newJob(MarkUnavailableJob.class)
                    .withIdentity("markUnavailableJob_" + entity.getRequest().getId())
                    .usingJobData("approvalHistoryId", entity.getRequest().getId())
                    .build();

            Trigger triggerInicio = TriggerBuilder.newTrigger()
                    .startAt(Date.from(entity.getRequest().getDateTimeStart().toInstant()))
                    .build();

            scheduler.scheduleJob(jobInicio, triggerInicio);

            JobDetail jobFim = JobBuilder.newJob(MarkAvailableJob.class)
                    .withIdentity("markAvailableJob_" + entity.getId())
                    .usingJobData("approvalHistoryId", entity.getId())
                    .build();

            Trigger triggerFim = TriggerBuilder.newTrigger()
                    .startAt(Date.from(entity.getRequest().getDateTimeEnd().toInstant()))
                    .build();

            scheduler.scheduleJob(jobFim, triggerFim);

        }

        return new ApprovalHistoryDTO(entity);
    }

    @Transactional
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
}
