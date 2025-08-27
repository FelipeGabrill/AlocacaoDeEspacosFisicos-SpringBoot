package com.ucsal.arqsoftware.queryfilters;

import com.ucsal.arqsoftware.entities.AuditLog;
import com.ucsal.arqsoftware.specifications.AuditSpec;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class AuditQueryFilter {

    private String username;
    private String action;

    public Specification<AuditLog> toSpecification() {
        return Specification.where(AuditSpec.usernameContains(username))
                .and(AuditSpec.actionContains(action));
    }
}