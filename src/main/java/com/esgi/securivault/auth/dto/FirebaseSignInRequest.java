package com.esgi.securivault.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FirebaseSignInRequest(

        @NotBlank(message = "L'email ne peut pas être vide")
        @Pattern(regexp = "^[^@]+@[^@]+$", message = "Format de l'email invalid")
        String email,

        @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
        String password
) {}



