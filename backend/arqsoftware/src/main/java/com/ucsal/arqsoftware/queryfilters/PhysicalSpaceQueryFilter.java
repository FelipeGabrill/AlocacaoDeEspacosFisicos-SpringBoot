package com.ucsal.arqsoftware.queryfilters;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.PhysicalSpaceType;
import com.ucsal.arqsoftware.specifications.PhysicalSpaceSpec;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class PhysicalSpaceQueryFilter {

    private String name;
    private PhysicalSpaceType type;
    private Integer capacity;
    private Boolean availability;

    public Specification<PhysicalSpace> toSpecification() {
        return Specification.where(PhysicalSpaceSpec.nameContains(name))
                .and(PhysicalSpaceSpec.hasType(type))
                .and(PhysicalSpaceSpec.hasCapacity(capacity))
                .and(PhysicalSpaceSpec.hasAvailability(availability));
    }
}
