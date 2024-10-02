package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.RequestStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RequestDTO {
	
	private Long id;
	
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateTimeStart;
	
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateTimeEnd;
	
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateCreationRequest;
	
	@NotBlank(message = "Necessidades não podem ser vazias")
	private String needs;
	
	@NotNull(message = "Status não pode ser nulo")
	private RequestStatus status;
	
	@Positive(message = "ID do espaço físico deve ser positivo")
	private Long physicalSpaceId;
	
	@Positive(message = "ID do usuário deve ser positivo")
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
