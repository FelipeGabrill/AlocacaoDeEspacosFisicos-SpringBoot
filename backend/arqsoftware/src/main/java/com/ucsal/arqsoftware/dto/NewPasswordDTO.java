package com.ucsal.arqsoftware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordDTO {

    @Schema(description = "Password recovery token sent to the user", example = "abc123token", required = true)
    private String token;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    @Schema(description = "New password for the user", example = "StrongPass123", required = true)
    private String password;
}