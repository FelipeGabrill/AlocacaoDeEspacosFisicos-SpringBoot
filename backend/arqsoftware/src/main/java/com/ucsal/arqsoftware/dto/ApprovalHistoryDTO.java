package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ucsal.arqsoftware.entities.ApprovalHistory;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApprovalHistoryDTO {
	
	private Long id;

	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateTime;
	
	private boolean decision;
	
	@Size(min = 5, message = "Observação precisa ter no minimo 5 caracteres")
	private String observation;
	
	@Positive(message = "ID do usuário deve ser positivo")
	private Long userId;
	
	private List<RequestDTO> requests = new ArrayList<>();
	
    public ApprovalHistoryDTO(ApprovalHistory entity) {
        id = entity.getId();
        dateTime = entity.getDateTime();
        decision = entity.isDecision();
        observation = entity.getObservation();
        userId = entity.getUser().getId();
        requests = entity.getRequests().stream()
	            .map(RequestDTO::new) 
	            .collect(Collectors.toList());
    }
}
