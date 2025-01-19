package com.challenge.forohub.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record crearUsuario(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank @Email String email
) {
}
