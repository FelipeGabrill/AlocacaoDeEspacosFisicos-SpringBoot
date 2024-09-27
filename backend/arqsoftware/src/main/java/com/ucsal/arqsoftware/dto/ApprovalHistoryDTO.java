package com.ucsal.arqsoftware.dto;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.entities.Request;

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
    private Set<Long> requestIds;

    public ApprovalHistoryDTO(ApprovalHistory entity) {
        this.id = entity.getId();
        this.dateTime = entity.getDateTime();
        this.decision = entity.isDecision();
        this.observation = entity.getObservation();
        this.userId = entity.getUser().getId();
        this.requestIds = entity.getRequests().stream()
            .map(Request::getId)
            .collect(Collectors.toSet());
    }
}
