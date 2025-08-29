package com.ucsal.arqsoftware.specifications;

import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.enums.RequestStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class RequestSpec {

    public static Specification<Request> titleContains(String title) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(title)) {
                return null;
            }
            return builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Request> hasStatus(RequestStatus status) {
        return (root, query, builder) -> {
            if (status == null) {
                return null;
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<Request> hasUserId(Long userId) {
        return (root, query, builder) -> {
            if (userId == null) {
                return null;
            }
            return builder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Request> orderByDateCreationAsc() {
        return (root, query, builder) -> {
            query.orderBy(builder.asc(root.get("dateCreationRequest")));
            return null;
        };
    }

    public static Specification<Request> orderByDateCreationDesc() {
        return (root, query, builder) -> {
            query.orderBy(builder.desc(root.get("dateCreationRequest")));
            return null;
        };
    }

}
