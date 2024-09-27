package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.List;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.PhysicalSpaceType;
import com.ucsal.arqsoftware.entities.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PhysicalSpaceDTO {
	
	private Long id;
	
	private String name;
	
	private String location;
	
	private PhysicalSpaceType type;
	
	private Integer capacity;

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
