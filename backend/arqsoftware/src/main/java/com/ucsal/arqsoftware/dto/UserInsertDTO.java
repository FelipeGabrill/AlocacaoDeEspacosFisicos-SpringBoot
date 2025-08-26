package com.ucsal.arqsoftware.dto;

import com.ucsal.arqsoftware.servicies.validation.UserInsertValid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @Schema(description = "Password of the user (minimum 8 characters)", example = "P@ssw0rd", required = true)
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
	private String password;
	
	public UserInsertDTO() {
		super();
	}
	
}
