package com.ucsal.arqsoftware.dto;

import java.util.Date;

import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.RequestStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RequestDTO {
	
	private Long id;
	
	@Size(min = 12, max = 40, message = "O título deve ter entre 12 e 40 caracteres.")
	@NotBlank(message = "Titulos não podem ser vazias")
	private String title;
	
	@NotNull(message = "A data de início não pode ser nula")
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateTimeStart;
	
	@NotNull(message = "A data de término não pode ser nula")
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateTimeEnd;
	
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateCreationRequest;
	
	@NotBlank(message = "Necessidades não podem ser vazias")
	private String needs;
	
	private RequestStatus status;
	
	@Positive(message = "ID do espaço físico deve ser positivo")
	@NotNull(message = "Campo de espaço não pode ser vazio")
	private Long physicalSpaceId;
	
	@Positive(message = "ID do usuário deve ser positivo")
	@NotNull(message = "Campo de usuário não pode ser vazio")
	private Long userId;
	
	private ApprovalHistoryDTO approvalHistory;

	public RequestDTO(Request entity) {
		id = entity.getId();
		title = entity.getTitle();
		dateTimeStart = entity.getDateTimeStart();
		dateTimeEnd = entity.getDateTimeEnd();
		dateCreationRequest = entity.getDateCreationRequest();
		needs = entity.getNeeds();
		status = entity.getStatus();
		physicalSpaceId = entity.getPhysicalSpace().getId();
		userId = entity.getUser().getId();
		approvalHistory = (entity.getApprovalHistory() == null) ? null : new ApprovalHistoryDTO(entity.getApprovalHistory());
	}
	
	
	
}
