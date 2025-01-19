package com.challenge.forohub.domain.usuario.dto;

import com.challenge.forohub.domain.usuario.Roles;

public record actualizarUsuario(
        String password,
        Roles roles,
        String nombre,
        String apellido,
        String email,
        Boolean enabled
) {
}
