package com.ucsal.arqsoftware.servicies.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.ucsal.arqsoftware.controller.exception.FieldMessage;
import com.ucsal.arqsoftware.dto.UserUpdateDTO;
import com.ucsal.arqsoftware.entities.User;
import com.ucsal.arqsoftware.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserUpdateValid ann) {
	}
	
	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		long userId = Long.parseLong(uriVars.get("id"));
		
		
		
		List<FieldMessage> list = new ArrayList<>();
		
		if (repository.existsByLogin(dto.getLogin())) {
            User existingUser = repository.findByLogin(dto.getLogin()).orElse(null);
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                list.add(new FieldMessage("login", "Login já existente"));
            }
        }
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
				.addConstraintViolation();
		}
		
		return list.isEmpty();
	}
	
}
