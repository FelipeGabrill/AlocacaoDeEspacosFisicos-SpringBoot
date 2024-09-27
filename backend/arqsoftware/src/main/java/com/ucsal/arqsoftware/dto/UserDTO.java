package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ucsal.arqsoftware.entities.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDTO {
	
	private Long id;

	private String username;
	
	private String login;
	
	private String password;
	
	private List<RequestDTO> requests = new ArrayList<>();
	
	public UserDTO(User entity) {
		id = entity.getId();
		username = entity.getUsername();
		login = entity.getLogin();
		password = entity.getPassword();
		requests = entity.getRequests().stream()
	            .map(RequestDTO::new) 
	            .collect(Collectors.toList());
	}
}
