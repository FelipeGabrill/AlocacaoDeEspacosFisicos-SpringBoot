package com.ucsal.arqsoftware.dto;

import com.ucsal.arqsoftware.servicies.validation.UserInsertValid;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UserInsertValid
public class UserInsertDTO extends UserDTO {
	
	private String password;
	
	public UserInsertDTO() {
		super();
	}
	
}
