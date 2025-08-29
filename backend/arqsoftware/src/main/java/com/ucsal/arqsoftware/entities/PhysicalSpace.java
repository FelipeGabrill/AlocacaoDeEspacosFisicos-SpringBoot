package com.ucsal.arqsoftware.entities;

import java.util.HashSet;
import java.util.Set;

import com.ucsal.arqsoftware.entities.enums.PhysicalSpaceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_physical_spaces", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhysicalSpace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Setter
    @Column(nullable = false, unique = true)
    private String name;
	
	@Setter
	private String location;
	
	@Setter
	private PhysicalSpaceType type;
	
	@Setter
	private Integer capacity;
	
	@Setter
	private Boolean availability;
	
	@Setter
	private String resources;
	
	@OneToMany(mappedBy = "physicalSpace", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Request> requests = new HashSet<>();
	
	public PhysicalSpace() {
	}
	
}
