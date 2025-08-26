package com.ucsal.arqsoftware.dto;

import java.util.Date;

import com.ucsal.arqsoftware.entities.ApprovalHistory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApprovalHistoryDTO {

    @Schema(description = "ID of the approval history", example = "1")
    private Long id;

    @Schema(description = "Date and time of the decision", example = "2025-08-25T14:00:00")
    @FutureOrPresent(message = "A data deve ser a atual ou uma data futura")
	private Date dateTime;

    @Schema(description = "Decision taken", example = "true", required = true)
    @NotNull(message = "A decisão não pode ser nula")
	private boolean decision;

    @Schema(description = "Observations about the decision", example = "Approved after review", minLength = 5)
    @Size(min = 5, message = "Observação precisa ter no minimo 5 caracteres")
	private String observation;

    @Schema(description = "ID of the user who made the decision", example = "10", required = true)
    @Positive(message = "ID do usuário deve ser positivo")
	@NotNull(message = "Campo de usuário não pode ser vazio")
	private Long userId;

    @Schema(description = "ID of the related request", example = "5", required = true)
    @Positive(message = "ID da solicitação deve ser positivo")
	@NotNull(message = "Campo de solicitação não pode ser vazio")
	private Long requestId;
	
    public ApprovalHistoryDTO(ApprovalHistory entity) {
        id = entity.getId();
        dateTime = entity.getDateTime();
        decision = entity.isDecision();
        observation = entity.getObservation();
        userId = entity.getUser().getId();
        requestId = entity.getRequest().getId();
    }
}
