package com.ucsal.arqsoftware.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ucsal.arqsoftware.entities.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDTO {

    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Full name of the user", example = "João Silva")
    @NotBlank(message = "Nome de usuário não pode ser vazio")
	private String usernameUser;

    @Schema(description = "Login of the user", example = "joaosilva@gmail.com")
    @NotBlank(message = "Login não pode ser vazio")
	private String login;

    @Schema(description = "Roles assigned to the user", accessMode = Schema.AccessMode.READ_ONLY)
    @Size(min = 1, message = "O usuário deve possuir pelo menos uma role")
	private Set<RoleDTO> roles = new HashSet<>();

    @Schema(description = "List of requests associated with the user", accessMode = Schema.AccessMode.READ_ONLY)
    private List<RequestDTO> requests = new ArrayList<>();

    @Schema(description = "List of approval histories associated with the user", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ApprovalHistoryDTO> approvalHistories = new ArrayList<>();

	
	public UserDTO(User entity) {
		id = entity.getId();
		usernameUser = entity.getUsernameUser();
		login = entity.getUsername();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
		requests = entity.getRequests().stream()
	            .map(RequestDTO::new) 
	            .collect(Collectors.toList());
		approvalHistories = entity.getApprovalHistories().stream()
	            .map(ApprovalHistoryDTO::new) 
	            .collect(Collectors.toList());
	}
}
