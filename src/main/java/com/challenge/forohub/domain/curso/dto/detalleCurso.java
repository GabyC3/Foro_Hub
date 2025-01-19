package com.challenge.forohub.domain.curso.dto;

import com.challenge.forohub.domain.curso.Categoria;
import com.challenge.forohub.domain.curso.Curso;

public record detalleCurso(
        Long id,
        String nombre,
        Categoria categoria,
        Boolean activo) {

    public detalleCurso(Curso curso){
            this(
                    curso.getId(),
                    curso.getName(),
                    curso.getCategoria(),
                    curso.getActivo());
    }
}
