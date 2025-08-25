package com.ucsal.arqsoftware.servicies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ucsal.arqsoftware.dto.ApprovalHistoryDTO;
import com.ucsal.arqsoftware.dto.RequestDTO;
import com.ucsal.arqsoftware.dto.RoleDTO;
import com.ucsal.arqsoftware.dto.UserDTO;
import com.ucsal.arqsoftware.dto.UserInsertDTO;
import com.ucsal.arqsoftware.dto.UserUpdateDTO;
import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.Role;
import com.ucsal.arqsoftware.entities.User;
import com.ucsal.arqsoftware.projections.UserDetailsProjection;
import com.ucsal.arqsoftware.repositories.RoleRepository;
import com.ucsal.arqsoftware.repositories.UserRepository;
import com.ucsal.arqsoftware.servicies.exceptions.DatabaseException;
import com.ucsal.arqsoftware.servicies.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new UserDTO(user);
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable) {
		Page<User> result = repository.findAll(pageable);
		return result.map(x -> new UserDTO(x));
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
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
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setUsernameUser(dto.getUsernameUser());
		entity.setLogin(dto.getLogin());

		for (RoleDTO roleDto : dto.getRoles()) {
			boolean exists = entity.getRoles().stream().anyMatch(role -> role.getId().equals(roleDto.getId()));
			if (!exists) {
				Role role = roleRepository.getReferenceById(roleDto.getId());
				entity.getRoles().add(role);
			}
		}

		for (RequestDTO reqDto : dto.getRequests()) {
			boolean exists = entity.getRequests().stream().anyMatch(req -> req.getId().equals(reqDto.getId()));
			if (!exists) {
				Request req = new Request();
				req.setId(reqDto.getId());
				entity.getRequests().add(req);
			}
		}

		for (ApprovalHistoryDTO aprDto : dto.getApprovalHistories()) {
			boolean exists = entity.getApprovalHistories().stream().anyMatch(apr -> apr.getId().equals(aprDto.getId()));
			if (!exists) {
				ApprovalHistory apr = new ApprovalHistory();
				apr.setId(aprDto.getId());
				entity.getApprovalHistories().add(apr);
			}
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

	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User user = authenticated();
		return new UserDTO(user);
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> getByLogin(String login, Pageable pageable) {
		Page<User> result = repository.findByLoginIgnoreCaseContaining(login, pageable);
		return result.map(UserDTO::new);
	}

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");

            return repository.findByLogin(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
