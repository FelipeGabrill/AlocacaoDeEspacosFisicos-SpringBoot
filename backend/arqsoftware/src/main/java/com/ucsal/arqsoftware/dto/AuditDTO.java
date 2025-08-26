package com.ucsal.arqsoftware.dto;

import java.time.LocalDateTime;

import com.ucsal.arqsoftware.entities.AuditLog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuditDTO {

    @Schema(description = "Unique identifier of the audit log", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Username of the user who performed the action", example = "john_doe@gmail.com", accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @Schema(description = "Type of action performed", example = "Created", accessMode = Schema.AccessMode.READ_ONLY)
    private String action;

    @Schema(description = "Detailed description of the action", example = "Created a user (ID: 15)", accessMode = Schema.AccessMode.READ_ONLY)
    private String details;

    @Schema(description = "Timestamp when the action was performed", example = "2025-08-25T20:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime timestamp;

    public AuditDTO(AuditLog entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.action = entity.getAction();
        this.timestamp = entity.getTimestamp();
        this.details = entity.getDetails();
    }
}
