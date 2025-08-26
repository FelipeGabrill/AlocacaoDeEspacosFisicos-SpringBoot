package com.ucsal.arqsoftware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    @Schema(description = "Email address of the user", example = "user@example.com", required = true)
    private String email;

}
