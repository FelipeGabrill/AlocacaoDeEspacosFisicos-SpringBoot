package com.ucsal.arqsoftware.dto;

import com.ucsal.arqsoftware.entities.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RoleDTO {
	
	private Long id;
	private String authority;
	
	public RoleDTO(Role role) {
		id = role.getId();
		authority = role.getAuthority();
	}
}
