package com.ucsal.arqsoftware.queryfilters;

import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.enums.RequestStatus;
import com.ucsal.arqsoftware.specifications.RequestSpec;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class RequestQueryFilter {

    private String title;
    private RequestStatus status;
    private Long userId;
    private Boolean orderByDateAsc;

    public Specification<Request> toSpecification() {
        Specification<Request> spec = Specification.where(RequestSpec.titleContains(title))
                .and(RequestSpec.hasStatus(status))
                .and(RequestSpec.hasUserId(userId));

        if (orderByDateAsc != null) {
            if (orderByDateAsc) {
                spec = spec.and(RequestSpec.orderByDateCreationAsc());
            } else {
                spec = spec.and(RequestSpec.orderByDateCreationDesc());
            }
        }

        return spec;
    }
}
