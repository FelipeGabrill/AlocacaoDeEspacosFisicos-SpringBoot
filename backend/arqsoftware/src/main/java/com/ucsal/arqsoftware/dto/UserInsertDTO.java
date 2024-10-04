package com.ucsal.arqsoftware.dto;

import com.ucsal.arqsoftware.servicies.validation.UserInsertValid;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UserInsertValid
public class UserInsertDTO extends UserDTO {
	
	@Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
	private String password;
	
	public UserInsertDTO() {
		super();
	}
	
}
