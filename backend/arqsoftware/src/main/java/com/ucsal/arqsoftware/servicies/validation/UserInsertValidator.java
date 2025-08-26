package com.ucsal.arqsoftware.servicies.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ucsal.arqsoftware.controllers.exception.FieldMessage;
import com.ucsal.arqsoftware.dto.UserInsertDTO;
import com.ucsal.arqsoftware.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}
	
	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if (repository.existsByLogin(dto.getLogin())) {
            list.add(new FieldMessage("login", "Login já existente"));
        }
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
				.addConstraintViolation();
		}
		
		return list.isEmpty();
	}
	
}
