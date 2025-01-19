package com.challenge.forohub.domain.usuario.dto;

import com.challenge.forohub.domain.usuario.Roles;
import com.challenge.forohub.domain.usuario.Usuario;

public record detalleUsuario(
        Long id,
        String username,
        Roles roles,
        String nombre,
        String apellido,
        String email,
        Boolean enabled
) {
    public detalleUsuario(Usuario usuario){
        this(usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
    }
}
