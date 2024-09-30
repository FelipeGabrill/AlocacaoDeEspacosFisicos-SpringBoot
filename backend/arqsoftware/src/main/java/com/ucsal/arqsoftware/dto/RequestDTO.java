package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	private Long physicalSpaceId;
	
	private Long userId;
	
	private List<ApprovalHistoryDTO> approvalHistories = new ArrayList<>();

	public RequestDTO(Request entity) {
		id = entity.getId();
		dateTimeStart = entity.getDateTimeStart();
		dateTimeEnd = entity.getDateTimeEnd();
		dateCreationRequest = entity.getDateCreationRequest();
		needs = entity.getNeeds();
		status = entity.getStatus();
		physicalSpaceId = entity.getPhysicalSpace().getId();
		userId = entity.getUser().getId();
		for (ApprovalHistory apr : entity.getApprovalHistories()) {
			approvalHistories.add(new ApprovalHistoryDTO(apr));
		}
	}
	
	
	
}
