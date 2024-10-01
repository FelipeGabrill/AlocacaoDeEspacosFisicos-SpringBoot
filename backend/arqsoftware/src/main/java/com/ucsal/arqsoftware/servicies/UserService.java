package com.ucsal.arqsoftware.servicies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ucsal.arqsoftware.dto.ApprovalHistoryDTO;
import com.ucsal.arqsoftware.dto.RequestDTO;
import com.ucsal.arqsoftware.dto.UserDTO;
import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.Role;
import com.ucsal.arqsoftware.entities.User;
import com.ucsal.arqsoftware.projections.UserDetailsProjection;
import com.ucsal.arqsoftware.repositories.UserRepository;
import com.ucsal.arqsoftware.servicies.exceptions.DatabaseException;
import com.ucsal.arqsoftware.servicies.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	public UserDTO findById(Long id) {
		User user = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new UserDTO(user);
	}
	
	public Page<UserDTO> findAll(Pageable pageable) {
		Page<User> result = repository.findAll(pageable);
		return result.map(x -> new UserDTO(x));
	}
	
	public UserDTO insert(UserDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new UserDTO(entity);
	}
	
	public UserDTO update(Long id, UserDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

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

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setUsername(dto.getUsername());
		entity.setLogin(dto.getLogin());
		entity.setPassword(dto.getPassword());
		entity.getRequests().clear();
		for (RequestDTO reqDto : dto.getRequests()) {
			Request req = new Request();
			req.setId(reqDto.getId());
			entity.getRequests().add(req);
		}
		for (ApprovalHistoryDTO aprDto : dto.getApprovalHistories()) {
			ApprovalHistory apr = new ApprovalHistory();
			apr.setId(aprDto.getId());
			entity.getApprovalHistories().add(apr);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = repository.searchUserAndRolesByLogin(username);
		if (result.size() == 0) {
			throw new UsernameNotFoundException("User not found");
		}
		
		User user = new User();
		user.setLogin(username);
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	}
}
