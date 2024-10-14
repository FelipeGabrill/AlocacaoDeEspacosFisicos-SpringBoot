package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ucsal.arqsoftware.entities.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDTO {
	
	private Long id;

	@NotBlank(message = "Nome de usuário não pode ser vazio")
	private String usernameUser;
	
	@NotBlank(message = "Login não pode ser vazio")
	private String login;
	
	@Size(min = 1, message = "O usuário deve possuir pelo menos uma role")
	private Set<RoleDTO> roles = new HashSet<>();
	
	private List<RequestDTO> requests = new ArrayList<>();
	
	private List<ApprovalHistoryDTO> approvalHistories = new ArrayList<>();

	
	public UserDTO(User entity) {
		id = entity.getId();
		usernameUser = entity.getUsernameUser();
		login = entity.getUsername();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
		requests = entity.getRequests().stream()
	            .map(RequestDTO::new) 
	            .collect(Collectors.toList());
		approvalHistories = entity.getApprovalHistories().stream()
	            .map(ApprovalHistoryDTO::new) 
	            .collect(Collectors.toList());
	}
}
