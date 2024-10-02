package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.List;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.PhysicalSpaceType;
import com.ucsal.arqsoftware.entities.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PhysicalSpaceDTO {
	
	private Long id;
	
	@NotBlank(message = "Nome não pode ser vazio")
	private String name;
	
	@NotBlank(message = "Localização não pode ser vazia")
	private String location;
	
	@NotNull(message = "Tipo não pode ser nulo")
	private PhysicalSpaceType type;
	
	@Positive(message = "Capacidade deve ser positiva")
	private Integer capacity;

	@Size(max = 255, message = "Recursos não podem ter mais de 255 caracteres")
	private String resources;
	
	private List<RequestDTO> requests = new ArrayList<>();
	
	public PhysicalSpaceDTO(PhysicalSpace entity) {
		id = entity.getId();
		name = entity.getName();
		location = entity.getLocation();
		type = entity.getType();
		capacity = entity.getCapacity();
		resources = entity.getResources();
		for(Request req : entity.getRequests()) {
			RequestDTO reqDTO = new RequestDTO(req);
			requests.add(reqDTO);
		}	
	}
}
