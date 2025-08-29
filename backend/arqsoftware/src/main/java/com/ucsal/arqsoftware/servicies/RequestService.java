package com.ucsal.arqsoftware.servicies;

import java.time.Instant;
import java.util.Date;

import com.ucsal.arqsoftware.queryfilters.RequestQueryFilter;
import com.ucsal.arqsoftware.servicies.exceptions.AccessDeniedException;
import com.ucsal.arqsoftware.servicies.exceptions.InvalidRequestStateException;
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
import com.ucsal.arqsoftware.entities.enums.RequestStatus;
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

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public RequestDTO findById(Long id) {
        Request request = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Requisição não encontrada"));
        return new RequestDTO(request);
    }

    @Transactional(readOnly = true)
    public Page<RequestDTO> findAll(RequestQueryFilter filter, Pageable pageable) {
        Page<Request> result = repository.findAll(filter.toSpecification(), pageable);
        return result.map(RequestDTO::new);
    }

    @Transactional
    public RequestDTO insert(RequestDTO dto) {
        PhysicalSpace physicalSpace = physicalSpaceRepository.getReferenceById(dto.getPhysicalSpaceId());
        checkConflictPhysicalSpace(physicalSpace, dto.getDateTimeStart(), dto.getDateTimeEnd());

        checkUserHasPendingRequest(dto.getUserId());

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

            User loggedUser = userService.authenticated();
            checkOwnerOrAdmin(entity, loggedUser);
            checkConflictPhysicalSpace(entity.getPhysicalSpace(), entity.getDateTimeStart(), entity.getDateTimeEnd());
            checkPendingStatus(entity);

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

    private void checkConflictPhysicalSpace(PhysicalSpace space, Date start, Date end) {
        boolean conflict = repository.existsByPhysicalSpaceAndStatusAndDateTimeStartLessThanEqualAndDateTimeEndGreaterThanEqual(
                space,
                RequestStatus.APPROVED,
                end,
                start
        );

        if (conflict) {
            throw new DatabaseException("Já existe uma requisição aprovada neste espaço físico nesse horário.");
        }
    }

    private void checkUserHasPendingRequest(Long userId) {
        if (repository.existsByUserIdAndStatus(userId, RequestStatus.PENDING)) {
            throw new InvalidRequestStateException("Usuário já possui uma request pendente");
        }
    }

    private void checkOwnerOrAdmin(Request request, User loggedUser) {
        boolean isOwner = request.getUser().getId().equals(loggedUser.getId());
        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Você não pode atualizar esta requisição");
        }
    }

    private void checkPendingStatus(Request request) {
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new InvalidRequestStateException("Não é possível atualizar: requisição não está pendente");
        }
    }
}