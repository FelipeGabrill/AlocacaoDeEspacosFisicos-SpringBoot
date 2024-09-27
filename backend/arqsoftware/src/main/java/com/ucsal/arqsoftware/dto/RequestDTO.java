package com.ucsal.arqsoftware.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.RequestStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RequestDTO {
	
	private Long id;
	
	private Date dateTimeStart;
	
	private Date dateTimeEnd;
	
	private Date dateCreationRequest;
	
	private String needs;
	
	private RequestStatus status;
	
	private PhysicalSpaceDTO physicalSpace;
	
	private UserDTO user;
	
	private Set<ApprovalHistory> approvalHistory = new HashSet<>();

	public RequestDTO(Request entity) {
		id = entity.getId();
		dateTimeStart = entity.getDateTimeStart();
		dateTimeEnd = entity.getDateTimeEnd();
		dateCreationRequest = entity.getDateCreationRequest();
		needs = entity.getNeeds();
		status = entity.getStatus();
		physicalSpace = new PhysicalSpaceDTO(entity.getPhysicalSpace());
		user = new UserDTO(entity.getUser());
		approvalHistory = entity.getApprovalHistory();
	}
	
	
	
}
