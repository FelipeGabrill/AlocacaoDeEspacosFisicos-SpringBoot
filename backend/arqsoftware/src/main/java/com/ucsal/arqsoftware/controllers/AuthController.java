package com.ucsal.arqsoftware.controllers;

import com.ucsal.arqsoftware.dto.EmailDTO;
import com.ucsal.arqsoftware.dto.NewPasswordDTO;
import com.ucsal.arqsoftware.servicies.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = "application/json")
@Tag(name = "Authentication", description = "Endpoints for password recovery and reset")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Create Password Recovery Token",
            description = "Generate a token to recover the password for the given email",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content – token created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request – invalid email format"),
                    @ApiResponse(responseCode = "404", description = "Not Found – email not registered")
            }
    )
    @PostMapping("/recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO dto) {
        authService.createRecoverToken(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Reset Password",
            description = "Reset the password using a valid recovery token",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content – password updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request – invalid token or password"),
                    @ApiResponse(responseCode = "404", description = "Not Found – token not valid")
            }
    )
    @PutMapping("/new-password")
    public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO dto) {
        authService.saveNewPassword(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Validate Recovery Token",
            description = "Check whether a password recovery token is valid",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK – returns true if token is valid"),
                    @ApiResponse(responseCode = "400", description = "Bad Request – token missing or invalid format")
            }
    )
    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@Parameter(description = "Recovery token to validate", required = true)
                                                     @RequestParam String token) {
        boolean valid = authService.isValidToken(token);
        return ResponseEntity.ok(valid);
    }
}
