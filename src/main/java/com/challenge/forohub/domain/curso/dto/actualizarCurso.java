package com.challenge.forohub.domain.curso.dto;

import com.challenge.forohub.domain.curso.Categoria;

public record actualizarCurso(
        String nombre,
        Categoria categoria,
        Boolean activo) {
}