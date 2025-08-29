package com.ucsal.arqsoftware.specifications;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.enums.PhysicalSpaceType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class PhysicalSpaceSpec {

    public static Specification<PhysicalSpace> nameContains(String name) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)) {
                return null;
            }
            return builder.like(root.get("name"), "%" + name + "%");
        };
    }

    public static Specification<PhysicalSpace> hasType(PhysicalSpaceType type) {
        return (root, query, builder) -> {
            if (type == null) {
                return null;
            }
            return builder.equal(root.get("type"), type);
        };
    }

    public static Specification<PhysicalSpace> hasCapacity(Integer capacity) {
        return (root, query, builder) -> {
            if (capacity == null) {
                return null;
            }
            return builder.equal(root.get("capacity"), capacity);
        };
    }

    public static Specification<PhysicalSpace> hasAvailability(Boolean availability) {
        return (root, query, builder) -> {
            if (availability == null) {
                return null;
            }
            return builder.equal(root.get("availability"), availability);
        };
    }
}
