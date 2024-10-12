package com.ucsal.arqsoftware.dto;

import java.time.LocalDateTime;

import com.ucsal.arqsoftware.entities.AuditLog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuditDTO {

    private Long id;
    private String username;
    private String action;
    private LocalDateTime timestamp;
    private String details;

    public AuditDTO(AuditLog entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.action = entity.getAction();
        this.timestamp = entity.getTimestamp();
        this.details = entity.getDetails();
    }
}
