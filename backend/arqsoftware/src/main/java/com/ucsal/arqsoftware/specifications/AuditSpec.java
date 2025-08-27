package com.ucsal.arqsoftware.specifications;

import com.ucsal.arqsoftware.entities.AuditLog;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class AuditSpec {

    public static Specification<AuditLog> usernameContains(String username) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(username)) {
                return null;
            }
            return builder.like(root.get("username"), "%" + username + "%");
        };
    }

    public static Specification<AuditLog> actionContains(String action) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(action)) {
                return null;
            }
            return builder.like(root.get("action"), "%" + action + "%");
        };
    }
}