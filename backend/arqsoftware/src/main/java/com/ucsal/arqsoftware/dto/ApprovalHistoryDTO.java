package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ucsal.arqsoftware.entities.ApprovalHistory;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApprovalHistoryDTO {
	
	private Long id;

	private Date dateTime;
	
	private boolean decision;
	
	private String observation;
	
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
