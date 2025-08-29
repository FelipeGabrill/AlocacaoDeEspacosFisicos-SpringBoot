package com.ucsal.arqsoftware.dto;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.entities.Request;
import com.ucsal.arqsoftware.entities.enums.PhysicalSpaceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class PhysicalSpaceSimpleDTO {

    @Schema(description = "Unique identifier of the physical space", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the physical space", example = "Room 101", required = true)
    @NotBlank(message = "Nome não pode ser vazio")
	private String name;

    @Schema(description = "Location of the physical space", example = "First Floor", required = true)
    @NotBlank(message = "Localização não pode ser vazia")
	private String location;

    @Schema(description = "Type of the physical space", example = "CLASSROOM", required = true)
    @NotNull(message = "Tipo não pode ser nulo")
	private PhysicalSpaceType type;

    @Schema(description = "Maximum capacity of the physical space", example = "50", required = true)
    @Positive(message = "Capacidade deve ser positiva")
	@NotNull(message = "Capacidade não pode ser vazia")
	private Integer capacity;

    @Schema(description = "Available resources in the physical space", example = "Projector, Whiteboard", required = true)
    @NotBlank(message = "Recursos não pode ser vazio")
	@Size(max = 255, message = "Recursos não podem ter mais de 255 caracteres")
	private String resources;

    @Schema(description = "Availability status of the physical space", example = "true", required = true)
    @Setter
	private Boolean availability;

	public PhysicalSpaceSimpleDTO(PhysicalSpace entity) {
		id = entity.getId();
		name = entity.getName();
		location = entity.getLocation();
		type = entity.getType();
		capacity = entity.getCapacity();
		resources = entity.getResources();
		availability = entity.getAvailability();
	}
}
