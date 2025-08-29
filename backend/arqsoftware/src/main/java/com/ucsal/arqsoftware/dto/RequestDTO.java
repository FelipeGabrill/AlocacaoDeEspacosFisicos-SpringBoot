package com.ucsal.arqsoftware.dto;

import java.util.Date;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.enums.RequestStatus;

import com.ucsal.arqsoftware.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Unique identifier of the request", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Title of the request", example = "Request for Room 101", required = true)
    @Size(min = 12, max = 40, message = "O título deve ter entre 12 e 40 caracteres.")
	@NotBlank(message = "Titulos não podem ser vazias")
	private String title;

    @Schema(description = "Start date and time of the request", example = "2025-12-10T15:00:00", required = true)
    @NotNull(message = "A data de início não pode ser nula")
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateTimeStart;

    @Schema(description = "End date and time of the request", example = "2025-12-10T17:00:00", required = true)
    @NotNull(message = "A data de término não pode ser nula")
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateTimeEnd;

    @Schema(description = "Date when the request was created", example = "2025-08-25T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
	@FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateCreationRequest;

    @Schema(description = "Description of the user's needs for the request", example = "Projector needed for presentation", required = true)
    @NotBlank(message = "Necessidades não podem ser vazias")
	private String needs;

    @Schema(description = "Status of the request", example = "PENDING", accessMode = Schema.AccessMode.READ_ONLY)
    private RequestStatus status;

    @Schema(description = "ID of the physical space associated with the request", example = "2", required = true)
    @Positive(message = "ID do espaço físico deve ser positivo")
	@NotNull(message = "Campo de espaço não pode ser vazio")
	private Long physicalSpaceId;

    @Schema(description = "ID of the user making the request", example = "1", required = true)
    @Positive(message = "ID do usuário deve ser positivo")
	@NotNull(message = "Campo de usuário não pode ser vazio")
	private Long userId;

    @Schema(description = "Approval history associated with the request", accessMode = Schema.AccessMode.READ_ONLY)
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


    public RequestDTO(Request r, User user, PhysicalSpace physicalSpace) {
    }
}
