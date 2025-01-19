package com.challenge.forohub.domain.curso.dto;

import com.challenge.forohub.domain.curso.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record crearCurso(
        @NotBlank String nombre,
        @NotNull Categoria categoria) {
}
